package com.fx.exchangerate.application;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.application.port.out.PublishExchangeRatePort;
import com.fx.exchangerate.domain.CurrencyPair;
import com.fx.exchangerate.domain.ExchangeRate;
import com.fx.exchangerate.domain.RateSource;

public record SimulateExchangeRateUseCase(ExchangeRateStatePort exchangeRateStatePort,
		PublishExchangeRatePort publishExchangeRatePort) {

	private static final Logger log = LoggerFactory.getLogger(SimulateExchangeRateUseCase.class);

	private static final SecureRandom RANDOM = new SecureRandom();

	public void execute() {
		BigDecimal base = exchangeRateStatePort.getLastPublishedRate()
				.or(exchangeRateStatePort::getLastApiRate)
				.orElse(null);
		if (base == null) {
			log.debug("simulation skipped: no base rate");
			return;
		}
		double jitter = 0.995 + RANDOM.nextDouble() * 0.01;
		BigDecimal simulated = base.multiply(BigDecimal.valueOf(jitter)).setScale(8, RoundingMode.HALF_UP);
		Instant ts = Instant.now();
		ExchangeRate rate = ExchangeRate.create(CurrencyPair.usdBrl(), simulated, ts, RateSource.SIMULATION);
		exchangeRateStatePort.setLastPublishedRate(simulated);
		log.info("published simulated exchange rate {}", rate.rate());
		publishExchangeRatePort.publish(rate);
	}
}
