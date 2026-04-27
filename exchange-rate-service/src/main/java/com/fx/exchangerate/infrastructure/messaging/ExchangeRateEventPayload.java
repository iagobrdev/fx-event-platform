package com.fx.exchangerate.infrastructure.messaging;

import java.math.BigDecimal;
import java.time.Instant;

import com.fx.exchangerate.domain.ExchangeRate;
import com.fx.exchangerate.domain.RateSource;

public record ExchangeRateEventPayload(String pair, BigDecimal rate, Instant timestamp, RateSource source) {

	public static ExchangeRateEventPayload from(ExchangeRate rate) {
		return new ExchangeRateEventPayload(rate.pair().value(), rate.rate(), rate.timestamp(), rate.source());
	}
}
