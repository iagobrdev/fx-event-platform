package com.fx.exchangerate.application;

import java.math.BigDecimal;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fx.exchangerate.application.port.out.AwesomeFxRatesPort;
import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.application.port.out.PublishExchangeRatePort;
import com.fx.exchangerate.domain.CurrencyPair;
import com.fx.exchangerate.domain.ExchangeRate;
import com.fx.exchangerate.domain.RateSource;

public record FetchExchangeRateUseCase(AwesomeFxRatesPort awesomeFxRatesPort, ExchangeRateStatePort exchangeRateStatePort,
		PublishExchangeRatePort publishExchangeRatePort) {

	private static final Logger log = LoggerFactory.getLogger(FetchExchangeRateUseCase.class);

	public void execute() {
		var fromApi = awesomeFxRatesPort.fetchUsdBrlBid();
		if (fromApi.isPresent()) {
			BigDecimal v = fromApi.get();
			exchangeRateStatePort.setLastApiRate(v);
			Instant ts = Instant.now();
			ExchangeRate rate = ExchangeRate.create(CurrencyPair.usdBrl(), v, ts, RateSource.API);
			exchangeRateStatePort.setLastPublishedRate(v);
			log.info("published api exchange rate {}", rate.rate());
			publishExchangeRatePort.publish(rate);
			return;
		}
		BigDecimal fallback = exchangeRateStatePort.getLastApiRate().orElse(null);
		if (fallback == null) {
			log.warn("fetch skipped: api unavailable and no last api rate");
			return;
		}
		Instant ts = Instant.now();
		ExchangeRate rate = ExchangeRate.create(CurrencyPair.usdBrl(), fallback, ts, RateSource.API);
		exchangeRateStatePort.setLastPublishedRate(fallback);
		log.warn("published cached api exchange rate {}", rate.rate());
		publishExchangeRatePort.publish(rate);
	}
}
