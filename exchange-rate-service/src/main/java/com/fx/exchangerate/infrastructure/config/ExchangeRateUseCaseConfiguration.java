package com.fx.exchangerate.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fx.exchangerate.application.FetchExchangeRateUseCase;
import com.fx.exchangerate.application.port.out.AvailableFxPairsPort;
import com.fx.exchangerate.application.port.out.AwesomeFxRatesPort;
import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.application.port.out.PublishExchangeRatePort;

@Configuration
public class ExchangeRateUseCaseConfiguration {

	@Bean
	public FetchExchangeRateUseCase fetchExchangeRateUseCase(AvailableFxPairsPort availableFxPairsPort,
			AwesomeFxRatesPort awesomeFxRatesPort, ExchangeRateStatePort exchangeRateStatePort,
			PublishExchangeRatePort publishExchangeRatePort) {
		return new FetchExchangeRateUseCase(availableFxPairsPort, awesomeFxRatesPort, exchangeRateStatePort,
				publishExchangeRatePort);
	}
}
