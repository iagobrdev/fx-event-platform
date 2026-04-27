package com.fx.exchangerate.infrastructure.state;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

class InMemoryExchangeRateStateTest {

	@Test
	void tracksApiAndPublished() {
		InMemoryExchangeRateState state = new InMemoryExchangeRateState();
		state.setLastApiRate("USD/BRL", new BigDecimal("5"));
		state.setLastPublishedRate("USD/BRL", new BigDecimal("5.01"));
		assertThat(state.getLastApiRate("USD/BRL")).contains(new BigDecimal("5"));
		assertThat(state.getLastPublishedRate("USD/BRL")).contains(new BigDecimal("5.01"));
		assertThat(state.allTrackedDisplayPairs()).containsExactlyInAnyOrder("USD/BRL");
	}
}
