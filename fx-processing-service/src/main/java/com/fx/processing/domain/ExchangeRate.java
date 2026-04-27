package com.fx.processing.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record ExchangeRate(CurrencyPair pair, BigDecimal rate, Instant timestamp, RateSource source) {

	public ExchangeRate {
		if (pair == null || rate == null || timestamp == null || source == null) {
			throw new IllegalArgumentException("invalid exchange rate");
		}
		if (rate.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("rate must be positive");
		}
	}

	public static ExchangeRate restore(CurrencyPair pair, BigDecimal rate, Instant timestamp, RateSource source) {
		return new ExchangeRate(pair, rate, timestamp, source);
	}
}
