package com.fx.gateway.application;

import java.util.Optional;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.ExchangeRateSnapshot;
import com.fx.gateway.domain.ParsedCurrencyPair;

public record GetLatestRateUseCase(ExchangeRateReadPort exchangeRateReadPort) {

	public Optional<ExchangeRateSnapshot> execute(String pair) {
		ParsedCurrencyPair parsed = ParsedCurrencyPair.parse(pair);
		return exchangeRateReadPort.findLatestByPair(parsed.display());
	}
}
