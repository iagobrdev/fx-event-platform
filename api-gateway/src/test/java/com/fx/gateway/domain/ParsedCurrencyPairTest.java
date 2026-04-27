package com.fx.gateway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class ParsedCurrencyPairTest {

	@Test
	void parsesAndDisplays() {
		ParsedCurrencyPair p = ParsedCurrencyPair.parse("usd/brl");
		assertThat(p.base().value()).isEqualTo("USD");
		assertThat(p.quote().value()).isEqualTo("BRL");
		assertThat(p.display()).isEqualTo("USD/BRL");
	}

	@Test
	void rejectsInvalid() {
		assertThatThrownBy(() -> ParsedCurrencyPair.parse(null)).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> ParsedCurrencyPair.parse("USDBRL")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> ParsedCurrencyPair.parse("USD/")).isInstanceOf(IllegalArgumentException.class);
		assertThatThrownBy(() -> ParsedCurrencyPair.parse("USD/B/R")).isInstanceOf(IllegalArgumentException.class);
	}
}
