package com.fx.gateway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.ExchangeRateSnapshot;
import com.fx.gateway.domain.RateSource;

@ExtendWith(MockitoExtension.class)
class GetLatestRateUseCaseTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Mock
	private ExchangeRateReadPort exchangeRateReadPort;

	@Test
	void returnsSnapshotWhenPresent() {
		when(exchangeRateReadPort.findLatestByPair("USD/BRL"))
				.thenReturn(Optional.of(new ExchangeRateSnapshot("USD/BRL", new BigDecimal("5"), TS, RateSource.API, null)));
		GetLatestRateUseCase useCase = new GetLatestRateUseCase(exchangeRateReadPort);
		assertThat(useCase.execute("usd/brl")).isPresent();
	}

	@Test
	void rejectsInvalidPairString() {
		GetLatestRateUseCase useCase = new GetLatestRateUseCase(exchangeRateReadPort);
		assertThatThrownBy(() -> useCase.execute("USDBRL")).isInstanceOf(IllegalArgumentException.class);
	}
}
