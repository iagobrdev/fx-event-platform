package com.fx.exchangerate.infrastructure.schedule;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.fx.exchangerate.application.FetchExchangeRateUseCase;
import com.fx.exchangerate.application.port.out.AvailableFxPairsPort;
import com.fx.exchangerate.application.port.out.AwesomeFxRatesPort;
import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.application.port.out.PublishExchangeRatePort;

class ExchangeRateScheduleTest {

	@Test
	void delegatesToFetchUseCase() {
		AvailableFxPairsPort availableFxPairsPort = Mockito.mock(AvailableFxPairsPort.class);
		AwesomeFxRatesPort awesomeFxRatesPort = Mockito.mock(AwesomeFxRatesPort.class);
		ExchangeRateStatePort exchangeRateStatePort = Mockito.mock(ExchangeRateStatePort.class);
		PublishExchangeRatePort publishExchangeRatePort = Mockito.mock(PublishExchangeRatePort.class);
		when(availableFxPairsPort.awesomeHyphenPairs()).thenReturn(List.of());
		FetchExchangeRateUseCase fetch = new FetchExchangeRateUseCase(availableFxPairsPort, awesomeFxRatesPort, exchangeRateStatePort,
				publishExchangeRatePort);
		ExchangeRateSchedule schedule = new ExchangeRateSchedule(fetch);
		schedule.pollApi();
		verify(availableFxPairsPort).refreshIfStale();
		verify(availableFxPairsPort).awesomeHyphenPairs();
	}
}
