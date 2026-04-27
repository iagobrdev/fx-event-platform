package com.fx.processing.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fx.processing.application.ProcessExchangeRateUseCase;
import com.fx.processing.application.port.out.ExchangeRateStorePort;

@Configuration
public class ProcessingUseCaseConfiguration {

	@Bean
	public ProcessExchangeRateUseCase processExchangeRateUseCase(ExchangeRateStorePort exchangeRateStorePort) {
		return new ProcessExchangeRateUseCase(exchangeRateStorePort);
	}
}
