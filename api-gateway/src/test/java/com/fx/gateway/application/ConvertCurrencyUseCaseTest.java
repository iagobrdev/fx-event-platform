package com.fx.gateway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.CurrencyCode;
import com.fx.gateway.domain.ExchangeRateSnapshot;
import com.fx.gateway.domain.RateSource;

@ExtendWith(MockitoExtension.class)
class ConvertCurrencyUseCaseTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Mock
	private ExchangeRateReadPort exchangeRateReadPort;

	@Test
	void convertsUsingSnapshots() {
		when(exchangeRateReadPort.findLatestDistinctByPair()).thenReturn(
				List.of(new ExchangeRateSnapshot("USD/BRL", new BigDecimal("5"), TS, RateSource.API, null)));
		ConvertCurrencyUseCase useCase = new ConvertCurrencyUseCase(exchangeRateReadPort);
		assertThat(useCase.execute(CurrencyCode.of("USD"), CurrencyCode.of("BRL"), new BigDecimal("2"))).hasValueSatisfying(v -> assertThat(v).isEqualByComparingTo("10"));
	}

	@Test
	void rejectsNonPositiveAmount() {
		ConvertCurrencyUseCase useCase = new ConvertCurrencyUseCase(exchangeRateReadPort);
		assertThatThrownBy(() -> useCase.execute(CurrencyCode.of("USD"), CurrencyCode.of("BRL"), BigDecimal.ZERO))
				.isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> useCase.execute(CurrencyCode.of("USD"), CurrencyCode.of("BRL"), null)).isInstanceOf(IllegalArgumentException.class);
	}
}
