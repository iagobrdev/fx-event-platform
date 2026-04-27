package com.fx.gateway.domain;

import java.util.Locale;
import java.util.regex.Pattern;

public record CurrencyCode(String value) {

	private static final Pattern CODE = Pattern.compile("^[A-Za-z0-9]{2,15}$");

	public CurrencyCode {
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException("currency required");
		}
		String v = value.trim().toUpperCase(Locale.ROOT);
		if (!CODE.matcher(v).matches()) {
			throw new IllegalArgumentException("invalid currency code");
		}
		value = v;
	}

	public static CurrencyCode of(String raw) {
		return new CurrencyCode(raw);
	}
}
