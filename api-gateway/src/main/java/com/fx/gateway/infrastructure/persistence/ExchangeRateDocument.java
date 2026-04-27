package com.fx.gateway.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fx.gateway.domain.RateSource;

@Document("exchange_rates")
public record ExchangeRateDocument(
		@Id String id,
		String pair,
		BigDecimal rate,
		Instant timestamp,
		RateSource source,
		BigDecimal previousRate) {
}
