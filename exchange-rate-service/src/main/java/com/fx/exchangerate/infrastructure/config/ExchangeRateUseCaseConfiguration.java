package com.fx.exchangerate.infrastructure.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
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
			PublishExchangeRatePort publishExchangeRatePort,
			@Value("${fx.poll.fixed-usd-quotes:}") String fixedUsdQuotesRaw) {
		return new FetchExchangeRateUseCase(availableFxPairsPort, awesomeFxRatesPort, exchangeRateStatePort,
				publishExchangeRatePort, parseCsvUpper(fixedUsdQuotesRaw));
	}

	private static List<String> parseCsvUpper(String raw) {
		if (raw == null || raw.isBlank()) {
			return List.of();
		}
		String[] parts = raw.split(",");
		List<String> out = new ArrayList<>(parts.length);
		for (String p : parts) {
			if (p == null || p.isBlank()) {
				continue;
			}
			out.add(p.trim().toUpperCase());
		}
		return List.copyOf(out);
	}
}
