package com.fx.processing.infrastructure.persistence;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fx.processing.domain.RateSource;

@Document("exchange_rates")
@CompoundIndex(name = "pair_timestamp", def = "{'pair': 1, 'timestamp': 1}", unique = true)
public record ExchangeRateDocument(
		@Id String id,
		String pair,
		BigDecimal rate,
		Instant timestamp,
		RateSource source) {
}
