package com.fx.gateway.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.fx.gateway.application.ConvertCurrencyUseCase;
import com.fx.gateway.application.GetLatestRateUseCase;
import com.fx.gateway.application.port.out.ExchangeRateReadPort;

@SpringBootTest(classes = GatewayUseCaseConfiguration.class)
class GatewayUseCaseConfigurationTest {

	@MockBean
	private ExchangeRateReadPort exchangeRateReadPort;

	@Autowired
	private GetLatestRateUseCase getLatestRateUseCase;

	@Autowired
	private ConvertCurrencyUseCase convertCurrencyUseCase;

	@Test
	void exposesUseCaseBeans() {
		assertThat(getLatestRateUseCase).isNotNull();
		assertThat(convertCurrencyUseCase).isNotNull();
	}
}
