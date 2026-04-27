package com.fx.gateway.application;

import java.util.Optional;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.ExchangeRateSnapshot;

public record GetLatestRateUseCase(ExchangeRateReadPort exchangeRateReadPort) {

	public Optional<ExchangeRateSnapshot> execute(String pair) {
		return exchangeRateReadPort.findLatestByPair(pair);
	}
}
