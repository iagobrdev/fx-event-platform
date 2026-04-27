package com.fx.processing.infrastructure.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fx.processing.application.ProcessExchangeRateUseCase;
import com.fx.processing.application.port.out.ExchangeRateStorePort;

@SpringBootTest(classes = ProcessingUseCaseConfiguration.class)
class ProcessingUseCaseConfigurationTest {

	@MockBean
	private ExchangeRateStorePort exchangeRateStorePort;

	@Autowired
	private ProcessExchangeRateUseCase processExchangeRateUseCase;

	@Test
	void exposesUseCase() {
		assertThat(processExchangeRateUseCase).isNotNull();
	}
}
