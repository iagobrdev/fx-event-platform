package com.fx.exchangerate.domain;

public record CurrencyPair(String value) {

	public CurrencyPair {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("pair required");
		}
	}

	public static CurrencyPair of(String raw) {
		if (raw == null || raw.isBlank()) {
			throw new IllegalArgumentException("pair required");
		}
		return new CurrencyPair(raw.trim());
	}
}
