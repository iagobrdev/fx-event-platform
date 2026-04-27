package com.fx.exchangerate.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CurrencyPairTest {

	@Test
	void trimsValue() {
		assertThat(CurrencyPair.of("  USD/BRL  ").value()).isEqualTo("USD/BRL");
	}

	@Test
	void rejectsInvalid() {
		assertThatThrownBy(() -> CurrencyPair.of(null)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> CurrencyPair.of("")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> new CurrencyPair(null)).isInstanceOf(IllegalArgumentException.class);
	}
}
