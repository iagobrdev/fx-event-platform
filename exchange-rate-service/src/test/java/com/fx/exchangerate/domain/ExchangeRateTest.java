package com.fx.exchangerate.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

class ExchangeRateTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Test
	void createNormalizesRateScale() {
		ExchangeRate r = ExchangeRate.create(CurrencyPair.of("USD/BRL"), new BigDecimal("5.123456789"), TS, RateSource.API);
		assertThat(r.rate()).isEqualByComparingTo("5.12345679");
	}

	@Test
	void rejectsInvalid() {
		assertThatThrownBy(() -> ExchangeRate.create(CurrencyPair.of("USD/BRL"), BigDecimal.ZERO, TS, RateSource.API))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> ExchangeRate.create(null, BigDecimal.ONE, TS, RateSource.API)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void withSimulationChangesSource() {
		ExchangeRate r = ExchangeRate.create(CurrencyPair.of("USD/BRL"), new BigDecimal("5"), TS, RateSource.API);
		Instant ts2 = Instant.parse("2026-01-02T00:00:00Z");
		ExchangeRate s = r.withSimulation(new BigDecimal("5.01"), ts2);
		assertThat(s.source()).isEqualTo(RateSource.SIMULATION);
		assertThat(s.timestamp()).isEqualTo(ts2);
	}
}
