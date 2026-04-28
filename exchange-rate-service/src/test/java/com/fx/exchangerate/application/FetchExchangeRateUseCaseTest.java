package com.fx.exchangerate.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fx.exchangerate.application.port.out.AvailableFxPairsPort;
import com.fx.exchangerate.application.port.out.AwesomeFxRatesPort;
import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.application.port.out.PublishExchangeRatePort;

@ExtendWith(MockitoExtension.class)
class FetchExchangeRateUseCaseTest {

	@Mock
	private AvailableFxPairsPort availableFxPairsPort;

	@Mock
	private AwesomeFxRatesPort awesomeFxRatesPort;

	@Mock
	private ExchangeRateStatePort exchangeRateStatePort;

	@Mock
	private PublishExchangeRatePort publishExchangeRatePort;

	private FetchExchangeRateUseCase fetchExchangeRateUseCase;

	@BeforeEach
	void setUp() {
		fetchExchangeRateUseCase = new FetchExchangeRateUseCase(availableFxPairsPort, awesomeFxRatesPort, exchangeRateStatePort,
				publishExchangeRatePort, List.of());
	}

	@Test
	void doesNothingWhenCatalogEmpty() {
		when(availableFxPairsPort.awesomeHyphenPairs()).thenReturn(List.of());
		fetchExchangeRateUseCase.execute();
		verify(publishExchangeRatePort, never()).publish(any());
	}

	@Test
	void publishesWhenApiReturnsBid() {
		when(availableFxPairsPort.awesomeHyphenPairs()).thenReturn(List.of("USD-BRL"));
		when(awesomeFxRatesPort.fetchBidsForAwesomeHyphenPairs(anyList())).thenReturn(Map.of("USD/BRL", new BigDecimal("5")));
		fetchExchangeRateUseCase.execute();
		verify(publishExchangeRatePort).publish(any());
		verify(exchangeRateStatePort).setLastApiRate("USD/BRL", new BigDecimal("5"));
	}

	@Test
	void publishesCachedWhenApiMissingButCacheExists() {
		when(availableFxPairsPort.awesomeHyphenPairs()).thenReturn(List.of("USD-BRL"));
		when(awesomeFxRatesPort.fetchBidsForAwesomeHyphenPairs(anyList())).thenReturn(Map.of());
		when(exchangeRateStatePort.getLastApiRate("USD/BRL")).thenReturn(java.util.Optional.of(new BigDecimal("4.9")));
		fetchExchangeRateUseCase.execute();
		verify(publishExchangeRatePort).publish(any());
	}

	@Test
	void usesFixedUsdQuotesWithoutRefreshingCatalog() {
		fetchExchangeRateUseCase = new FetchExchangeRateUseCase(availableFxPairsPort, awesomeFxRatesPort, exchangeRateStatePort,
				publishExchangeRatePort, List.of("brl", " eur "));
		when(awesomeFxRatesPort.fetchBidsForAwesomeHyphenPairs(anyList()))
				.thenReturn(Map.of("USD/BRL", new BigDecimal("5"), "USD/EUR", new BigDecimal("0.9")));
		fetchExchangeRateUseCase.execute();
		verify(availableFxPairsPort, never()).refreshIfStale();
		verify(awesomeFxRatesPort).fetchBidsForAwesomeHyphenPairs(List.of("USD-BRL", "USD-EUR"));
		verify(publishExchangeRatePort, times(2)).publish(any());
	}
}
