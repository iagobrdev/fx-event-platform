package com.fx.processing.application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fx.processing.application.port.out.ExchangeRateStorePort;
import com.fx.processing.domain.ExchangeRate;

public record ProcessExchangeRateUseCase(ExchangeRateStorePort exchangeRateStorePort) {

	private static final Logger log = LoggerFactory.getLogger(ProcessExchangeRateUseCase.class);

	public void execute(ExchangeRate exchangeRate) {
		exchangeRateStorePort.upsert(exchangeRate);
		log.info("exchange rate upserted for pair {} at {}", exchangeRate.pair().value(), exchangeRate.timestamp());
	}
}
