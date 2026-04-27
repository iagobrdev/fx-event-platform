package com.fx.processing.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fx.processing.application.port.out.ExchangeRateStorePort;
import com.fx.processing.domain.CurrencyPair;
import com.fx.processing.domain.ExchangeRate;
import com.fx.processing.domain.RateSource;

class ProcessExchangeRateUseCaseTest {

	@Test
	void upsertsOnEveryEvent() {
		ExchangeRateStorePort store = Mockito.mock(ExchangeRateStorePort.class);
		ProcessExchangeRateUseCase useCase = new ProcessExchangeRateUseCase(store);
		ExchangeRate rate = ExchangeRate.restore(CurrencyPair.of("USD/BRL"), BigDecimal.valueOf(5.12), Instant.parse("2026-04-27T12:00:00Z"),
				RateSource.API);
		useCase.execute(rate);
		verify(store).upsert(any(ExchangeRate.class));
	}
}
