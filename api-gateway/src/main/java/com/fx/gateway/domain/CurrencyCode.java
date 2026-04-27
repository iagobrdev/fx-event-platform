package com.fx.gateway.domain;

public record CurrencyCode(String value) {

	public CurrencyCode {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("currency required");
		}
	}

	public static CurrencyCode of(String raw) {
		if (raw == null || raw.isBlank()) {
			throw new IllegalArgumentException("currency required");
		}
		return new CurrencyCode(raw.trim().toUpperCase());
	}
}
