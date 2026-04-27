package com.fx.gateway.application;

import java.math.BigDecimal;
import java.util.Optional;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.CurrencyCode;
import com.fx.gateway.domain.ExchangeRateSnapshot;
import com.fx.gateway.domain.ForexConverter;

public record ConvertCurrencyUseCase(ExchangeRateReadPort exchangeRateReadPort) {

	private static final String USD_BRL = "USD/BRL";

	public Optional<BigDecimal> execute(CurrencyCode from, CurrencyCode to, BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("amount must be positive");
		}
		Optional<ExchangeRateSnapshot> rate = exchangeRateReadPort.findLatestByPair(USD_BRL);
		if (rate.isEmpty()) {
			return Optional.empty();
		}
		BigDecimal usdBrl = rate.get().rate();
		return Optional.of(ForexConverter.convert(from, to, amount, usdBrl));
	}
}
