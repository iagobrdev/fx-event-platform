package com.fx.gateway.domain;

public record ParsedCurrencyPair(CurrencyCode base, CurrencyCode quote) {

	public static ParsedCurrencyPair parse(String raw) {
		if (raw == null || !raw.contains("/")) {
			throw new IllegalArgumentException("invalid pair");
		}
		String[] parts = raw.split("/");
		if (parts.length != 2) {
			throw new IllegalArgumentException("invalid pair");
		}
		return new ParsedCurrencyPair(CurrencyCode.of(parts[0]), CurrencyCode.of(parts[1]));
	}

	public String display() {
		return base.value() + "/" + quote.value();
	}
}
