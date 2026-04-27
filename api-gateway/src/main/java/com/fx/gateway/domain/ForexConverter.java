package com.fx.gateway.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class ForexConverter {

	private ForexConverter() {
	}

	public static BigDecimal convert(CurrencyCode from, CurrencyCode to, BigDecimal amount, BigDecimal usdBrl) {
		if (from.value().equals("USD") && to.value().equals("BRL")) {
			return amount.multiply(usdBrl).setScale(8, RoundingMode.HALF_UP);
		}
		if (from.value().equals("BRL") && to.value().equals("USD")) {
			return amount.divide(usdBrl, 8, RoundingMode.HALF_UP);
		}
		throw new IllegalArgumentException("unsupported conversion");
	}
}
