package com.fx.processing.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fx.processing.application.port.out.ExchangeRateStorePort;
import com.fx.processing.domain.ExchangeRate;

public record ProcessExchangeRateUseCase(ExchangeRateStorePort exchangeRateStorePort) {

	private static final Logger log = LoggerFactory.getLogger(ProcessExchangeRateUseCase.class);

	public void execute(ExchangeRate exchangeRate) {
		if (exchangeRateStorePort.existsByPairAndTimestamp(exchangeRate.pair(), exchangeRate.timestamp())) {
			log.debug("duplicate event ignored for pair {} at {}", exchangeRate.pair().value(), exchangeRate.timestamp());
			return;
		}
		exchangeRateStorePort.save(exchangeRate);
		log.info("exchange rate stored for pair {} at {}", exchangeRate.pair().value(), exchangeRate.timestamp());
	}
}
