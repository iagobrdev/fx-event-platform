package com.fx.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

class CurrencyBridgeTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Test
	void sameCurrencyReturnsAmountScaled() {
		Optional<BigDecimal> r = CurrencyBridge.convert(CurrencyCode.of("USD"), CurrencyCode.of("USD"), new BigDecimal("10.123456789"),
				List.of());
		assertThat(r).contains(new BigDecimal("10.12345679"));
	}

	@Test
	void emptySnapshotsReturnsEmpty() {
		assertThat(CurrencyBridge.convert(CurrencyCode.of("USD"), CurrencyCode.of("EUR"), BigDecimal.ONE, List.of())).isEmpty();
	}

	@Test
	void skipsMalformedAndNonPositiveRates() {
		List<ExchangeRateSnapshot> list = List.of(new ExchangeRateSnapshot("BADPAIR", BigDecimal.ONE, TS, RateSource.API),
				new ExchangeRateSnapshot("USD/BRL", BigDecimal.ZERO, TS, RateSource.API),
				new ExchangeRateSnapshot("USD/BRL", new BigDecimal("-1"), TS, RateSource.API));
		assertThat(CurrencyBridge.convert(CurrencyCode.of("USD"), CurrencyCode.of("BRL"), BigDecimal.ONE, list)).isEmpty();
	}

	@Test
	void directQuote() {
		List<ExchangeRateSnapshot> list = List.of(new ExchangeRateSnapshot("USD/BRL", new BigDecimal("5"), TS, RateSource.API));
		assertThat(CurrencyBridge.convert(CurrencyCode.of("USD"), CurrencyCode.of("BRL"), new BigDecimal("2"), list)).hasValueSatisfying(v -> assertThat(v).isEqualByComparingTo("10"));
	}

	@Test
	void inverseQuote() {
		List<ExchangeRateSnapshot> list = List.of(new ExchangeRateSnapshot("USD/BRL", new BigDecimal("5"), TS, RateSource.API));
		assertThat(CurrencyBridge.convert(CurrencyCode.of("BRL"), CurrencyCode.of("USD"), new BigDecimal("10"), list)).isPresent();
	}

	@Test
	void multiHopThroughBridge() {
		List<ExchangeRateSnapshot> list = List.of(new ExchangeRateSnapshot("USD/BRL", new BigDecimal("5"), TS, RateSource.API),
				new ExchangeRateSnapshot("EUR/BRL", new BigDecimal("5.5"), TS, RateSource.API));
		Optional<BigDecimal> out = CurrencyBridge.convert(CurrencyCode.of("USD"), CurrencyCode.of("EUR"), BigDecimal.ONE, list);
		assertThat(out).isPresent();
		assertThat(out.get()).isGreaterThan(BigDecimal.ZERO);
	}
}
