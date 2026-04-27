package com.fx.exchangerate.application;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fx.exchangerate.application.port.out.AvailableFxPairsPort;
import com.fx.exchangerate.application.port.out.AwesomeFxRatesPort;
import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.application.port.out.PublishExchangeRatePort;
import com.fx.exchangerate.domain.CurrencyPair;
import com.fx.exchangerate.domain.ExchangeRate;
import com.fx.exchangerate.domain.RateSource;

/**
 * Refreshes the pair catalog when stale, fetches live bid rates from the Awesome API in batches, updates in-memory
 * state, and publishes each successful or fallback rate to Kafka with {@link RateSource#API}.
 *
 * <p>Rules:
 * <ul>
 *   <li>If the catalog is empty after refresh, the use case exits without publishing.</li>
 *   <li>Pairs are processed in chunks of 45 hyphen keys (e.g. {@code USD-BRL}) per HTTP call to the quotes API.</li>
 *   <li>Hyphen keys are mapped to display form {@code BASE/QUOTE} (uppercase) for state, domain, and messaging.</li>
 *   <li>When the API returns a bid for a pair: last API rate and last published rate are updated; an event is published.</li>
 *   <li>When the API omits a pair but a cached last API rate exists: that value is republished as last published (stale quote path);
 *       if there is no cache, the pair is skipped for this run.</li>
 *   <li>All published events share the same {@link Instant} captured at the start of the run for that execution.</li>
 * </ul>
 */
public record FetchExchangeRateUseCase(AvailableFxPairsPort availableFxPairsPort, AwesomeFxRatesPort awesomeFxRatesPort,
		ExchangeRateStatePort exchangeRateStatePort, PublishExchangeRatePort publishExchangeRatePort) {

	private static final Logger log = LoggerFactory.getLogger(FetchExchangeRateUseCase.class);

	private static final int BATCH = 45;

	/**
	 * Runs one full poll cycle: refresh catalog, fetch quotes in batches, update state, publish events.
	 */
	public void execute() {
		availableFxPairsPort.refreshIfStale();
		List<String> paths = availableFxPairsPort.awesomeHyphenPairs();
		if (paths.isEmpty()) {
			log.warn("no pair catalog yet");
			return;
		}
		Instant ts = Instant.now();
		for (int i = 0; i < paths.size(); i += BATCH) {
			List<String> chunk = paths.subList(i, Math.min(i + BATCH, paths.size()));
			Map<String, BigDecimal> bids = awesomeFxRatesPort.fetchBidsForAwesomeHyphenPairs(new ArrayList<>(chunk));
			for (String hyphen : chunk) {
				String display = hyphenToDisplay(hyphen);
				BigDecimal v = bids.get(display);
				if (v != null) {
					exchangeRateStatePort.setLastApiRate(display, v);
					exchangeRateStatePort.setLastPublishedRate(display, v);
					ExchangeRate rate = ExchangeRate.create(CurrencyPair.of(display), v, ts, RateSource.API);
					log.debug("published api {} {}", display, rate.rate());
					publishExchangeRatePort.publish(rate);
					continue;
				}
				BigDecimal fallback = exchangeRateStatePort.getLastApiRate(display).orElse(null);
				if (fallback == null) {
					log.debug("fetch skipped for {}: no api row and no cache", display);
					continue;
				}
				exchangeRateStatePort.setLastPublishedRate(display, fallback);
				ExchangeRate rate = ExchangeRate.create(CurrencyPair.of(display), fallback, ts, RateSource.API);
				log.warn("published cached api {} {}", display, rate.rate());
				publishExchangeRatePort.publish(rate);
			}
		}
	}

	private static String hyphenToDisplay(String hyphen) {
		int idx = hyphen.indexOf('-');
		if (idx <= 0 || idx == hyphen.length() - 1) {
			return hyphen;
		}
		return hyphen.substring(0, idx).toUpperCase() + "/" + hyphen.substring(idx + 1).toUpperCase();
	}
}
