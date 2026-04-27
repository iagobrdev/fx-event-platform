package com.fx.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CurrencyCodeTest {

	@Test
	void normalizesToUpperCase() {
		assertThat(CurrencyCode.of("usd").value()).isEqualTo("USD");
	}

	@Test
	void rejectsBlank() {
		assertThatThrownBy(() -> CurrencyCode.of(" ")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> CurrencyCode.of(null)).isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void rejectsInvalidPattern() {
		assertThatThrownBy(() -> CurrencyCode.of("U")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> CurrencyCode.of("ABCDEFGHIJKLMNOP")).isInstanceOf(IllegalArgumentException.class);
	}
}
