package com.fx.exchangerate.presentation;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;

@WebMvcTest(controllers = ExchangeRateController.class)
class ExchangeRateControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ExchangeRateStatePort exchangeRateStatePort;

	@Test
	void returnsStateSorted() throws Exception {
		when(exchangeRateStatePort.allTrackedDisplayPairs()).thenReturn(Set.of("EUR/USD", "USD/BRL"));
		when(exchangeRateStatePort.getLastApiRate("EUR/USD")).thenReturn(Optional.of(new BigDecimal("1.1")));
		when(exchangeRateStatePort.getLastPublishedRate("EUR/USD")).thenReturn(Optional.of(new BigDecimal("1.11")));
		when(exchangeRateStatePort.getLastApiRate("USD/BRL")).thenReturn(Optional.empty());
		when(exchangeRateStatePort.getLastPublishedRate("USD/BRL")).thenReturn(Optional.of(new BigDecimal("5")));
		mockMvc.perform(get("/exchange-rate/state")).andExpect(status().isOk()).andExpect(jsonPath("$.pairs['EUR/USD'].lastApiRate").value(1.1))
				.andExpect(jsonPath("$.pairs['USD/BRL'].lastPublishedRate").value(5));
	}
}
