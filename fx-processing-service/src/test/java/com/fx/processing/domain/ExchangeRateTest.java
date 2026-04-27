package com.fx.processing.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

class ExchangeRateTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Test
	void rejectsNonPositiveRate() {
		assertThatThrownBy(() -> ExchangeRate.restore(CurrencyPair.of("USD/BRL"), BigDecimal.ZERO, TS, RateSource.API))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
