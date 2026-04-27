package com.fx.gateway.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fx.gateway.application.ConvertCurrencyUseCase;
import com.fx.gateway.application.GetLatestRateUseCase;
import com.fx.gateway.application.port.out.ExchangeRateReadPort;

@Configuration
public class GatewayUseCaseConfiguration {

	@Bean
	public GetLatestRateUseCase getLatestRateUseCase(ExchangeRateReadPort exchangeRateReadPort) {
		return new GetLatestRateUseCase(exchangeRateReadPort);
	}

	@Bean
	public ConvertCurrencyUseCase convertCurrencyUseCase(ExchangeRateReadPort exchangeRateReadPort) {
		return new ConvertCurrencyUseCase(exchangeRateReadPort);
	}
}
