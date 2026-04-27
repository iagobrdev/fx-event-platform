package com.fx.gateway.application;

import java.math.BigDecimal;
import java.util.Optional;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.CurrencyBridge;
import com.fx.gateway.domain.CurrencyCode;

public record ConvertCurrencyUseCase(ExchangeRateReadPort exchangeRateReadPort) {

	public Optional<BigDecimal> execute(CurrencyCode from, CurrencyCode to, BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("amount must be positive");
		}
		return CurrencyBridge.convert(from, to, amount, exchangeRateReadPort.findLatestDistinctByPair());
	}
}
