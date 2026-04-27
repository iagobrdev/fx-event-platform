package com.fx.exchangerate.infrastructure.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.fx.exchangerate.domain.CurrencyPair;
import com.fx.exchangerate.domain.ExchangeRate;
import com.fx.exchangerate.domain.RateSource;

class ExchangeRateEventPayloadTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Test
	void fromMapsFields() {
		ExchangeRate rate = ExchangeRate.create(CurrencyPair.of("USD/BRL"), new BigDecimal("5"), TS, RateSource.API);
		ExchangeRateEventPayload p = ExchangeRateEventPayload.from(rate);
		assertThat(p.pair()).isEqualTo("USD/BRL");
		assertThat(p.rate()).isEqualByComparingTo("5");
		assertThat(p.timestamp()).isEqualTo(TS);
		assertThat(p.source()).isEqualTo(RateSource.API);
	}
}
