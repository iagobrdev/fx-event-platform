package com.fx.exchangerate.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.application.port.out.PublishExchangeRatePort;

@ExtendWith(MockitoExtension.class)
class SimulateExchangeRateUseCaseTest {

	@Mock
	private ExchangeRateStatePort exchangeRateStatePort;

	@Mock
	private PublishExchangeRatePort publishExchangeRatePort;

	@Test
	void skipsWhenNoBaseRate() {
		when(exchangeRateStatePort.allTrackedDisplayPairs()).thenReturn(Set.of("USD/BRL"));
		when(exchangeRateStatePort.getLastPublishedRate("USD/BRL")).thenReturn(java.util.Optional.empty());
		when(exchangeRateStatePort.getLastApiRate("USD/BRL")).thenReturn(java.util.Optional.empty());
		SimulateExchangeRateUseCase useCase = new SimulateExchangeRateUseCase(exchangeRateStatePort, publishExchangeRatePort);
		useCase.execute();
		verify(publishExchangeRatePort, never()).publish(any());
	}

	@Test
	void publishesWhenBaseExists() {
		when(exchangeRateStatePort.allTrackedDisplayPairs()).thenReturn(Set.of("USD/BRL"));
		when(exchangeRateStatePort.getLastPublishedRate("USD/BRL")).thenReturn(java.util.Optional.of(new BigDecimal("5")));
		SimulateExchangeRateUseCase useCase = new SimulateExchangeRateUseCase(exchangeRateStatePort, publishExchangeRatePort);
		useCase.execute();
		verify(publishExchangeRatePort).publish(any());
	}
}
