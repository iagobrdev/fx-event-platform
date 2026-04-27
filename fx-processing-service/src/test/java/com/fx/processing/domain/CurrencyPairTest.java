package com.fx.processing.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CurrencyPairTest {

	@Test
	void acceptsSlashFormat() {
		assertThat(CurrencyPair.of("USD/BRL").value()).isEqualTo("USD/BRL");
	}

	@Test
	void rejectsInvalid() {
		assertThatThrownBy(() -> CurrencyPair.of("USDBRL")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> CurrencyPair.of(null)).isInstanceOf(IllegalArgumentException.class);
	}
}
