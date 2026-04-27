package com.fx.exchangerate.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fx.exchangerate.application.FetchExchangeRateUseCase;
import com.fx.exchangerate.application.SimulateExchangeRateUseCase;
import com.fx.exchangerate.application.port.out.AwesomeFxRatesPort;
import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.application.port.out.PublishExchangeRatePort;

@Configuration
public class ExchangeRateUseCaseConfiguration {

	@Bean
	public FetchExchangeRateUseCase fetchExchangeRateUseCase(AwesomeFxRatesPort awesomeFxRatesPort,
			ExchangeRateStatePort exchangeRateStatePort, PublishExchangeRatePort publishExchangeRatePort) {
		return new FetchExchangeRateUseCase(awesomeFxRatesPort, exchangeRateStatePort, publishExchangeRatePort);
	}

	@Bean
	public SimulateExchangeRateUseCase simulateExchangeRateUseCase(ExchangeRateStatePort exchangeRateStatePort,
			PublishExchangeRatePort publishExchangeRatePort) {
		return new SimulateExchangeRateUseCase(exchangeRateStatePort, publishExchangeRatePort);
	}
}
