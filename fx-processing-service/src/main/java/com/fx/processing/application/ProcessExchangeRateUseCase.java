package com.fx.processing.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fx.processing.application.port.out.ExchangeRateStorePort;
import com.fx.processing.domain.ExchangeRate;

/**
 * Persists an exchange rate received from the event stream (e.g. Kafka) into the configured store (e.g. MongoDB).
 *
 * <p>Rules:
 * <ul>
 *   <li>The inbound {@link ExchangeRate} must already satisfy domain invariants (positive rate, non-null pair,
 *       timestamp, source); invalid payloads should be rejected before this use case is invoked.</li>
 *   <li>Persistence is an upsert keyed by pair (and companion fields as defined by the adapter), so replays
 *       overwrite the latest document for that pair.</li>
 * </ul>
 */
public record ProcessExchangeRateUseCase(ExchangeRateStorePort exchangeRateStorePort) {

	private static final Logger log = LoggerFactory.getLogger(ProcessExchangeRateUseCase.class);

	/**
	 * @param exchangeRate validated domain rate to persist
	 */
	public void execute(ExchangeRate exchangeRate) {
		exchangeRateStorePort.upsert(exchangeRate);
		log.info("exchange rate upserted for pair {} at {}", exchangeRate.pair().value(), exchangeRate.timestamp());
	}
}
