package com.fx.gateway.presentation;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.ExchangeRateSnapshot;
import com.fx.gateway.domain.RateSource;
import com.fx.gateway.infrastructure.config.GatewayUseCaseConfiguration;

@WebMvcTest(controllers = RateController.class)
@Import({ GatewayUseCaseConfiguration.class, ApiExceptionHandler.class })
class RateControllerTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ExchangeRateReadPort exchangeRateReadPort;

	@Test
	void returnsRateWhenFound() throws Exception {
		when(exchangeRateReadPort.findLatestByPair("USD/BRL"))
				.thenReturn(Optional.of(new ExchangeRateSnapshot("USD/BRL", new BigDecimal("5"), TS, RateSource.API)));
		mockMvc.perform(get("/rates").param("pair", "USD/BRL")).andExpect(status().isOk()).andExpect(jsonPath("$.pair").value("USD/BRL"))
				.andExpect(jsonPath("$.rate").value(5));
	}

	@Test
	void returns404WhenMissing() throws Exception {
		when(exchangeRateReadPort.findLatestByPair("USD/BRL")).thenReturn(Optional.empty());
		mockMvc.perform(get("/rates").param("pair", "USD/BRL")).andExpect(status().isNotFound());
	}

	@Test
	void returns400WhenPairInvalid() throws Exception {
		when(exchangeRateReadPort.findLatestByPair(anyString())).thenReturn(Optional.empty());
		mockMvc.perform(get("/rates").param("pair", "INVALID")).andExpect(status().isBadRequest());
	}
}
