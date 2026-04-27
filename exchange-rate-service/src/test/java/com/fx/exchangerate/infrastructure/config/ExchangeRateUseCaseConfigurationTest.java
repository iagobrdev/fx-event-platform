package com.fx.exchangerate.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fx.exchangerate.application.FetchExchangeRateUseCase;
import com.fx.exchangerate.application.SimulateExchangeRateUseCase;
import com.fx.exchangerate.application.port.out.AvailableFxPairsPort;
import com.fx.exchangerate.application.port.out.AwesomeFxRatesPort;
import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.application.port.out.PublishExchangeRatePort;

@SpringBootTest(classes = ExchangeRateUseCaseConfiguration.class)
class ExchangeRateUseCaseConfigurationTest {

	@MockBean
	private AvailableFxPairsPort availableFxPairsPort;

	@MockBean
	private AwesomeFxRatesPort awesomeFxRatesPort;

	@MockBean
	private ExchangeRateStatePort exchangeRateStatePort;

	@MockBean
	private PublishExchangeRatePort publishExchangeRatePort;

	@Autowired
	private FetchExchangeRateUseCase fetchExchangeRateUseCase;

	@Autowired
	private SimulateExchangeRateUseCase simulateExchangeRateUseCase;

	@Test
	void exposesUseCases() {
		assertThat(fetchExchangeRateUseCase).isNotNull();
		assertThat(simulateExchangeRateUseCase).isNotNull();
	}
}
