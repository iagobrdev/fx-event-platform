package com.fx.processing.domain;

public record CurrencyPair(String value) {

	public CurrencyPair {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("pair required");
		}
		if (!value.contains("/")) {
			throw new IllegalArgumentException("invalid pair format");
		}
	}

	public static CurrencyPair of(String raw) {
		if (raw == null || raw.isBlank()) {
			throw new IllegalArgumentException("pair required");
		}
		return new CurrencyPair(raw.trim());
	}
}
