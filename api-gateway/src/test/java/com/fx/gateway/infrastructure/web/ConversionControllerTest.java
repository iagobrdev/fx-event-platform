package com.fx.gateway.infrastructure.web;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

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

@WebMvcTest(controllers = ConversionController.class)
@Import({ GatewayUseCaseConfiguration.class, ApiExceptionHandler.class })
class ConversionControllerTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ExchangeRateReadPort exchangeRateReadPort;

	@Test
	void convertsWhenPathExists() throws Exception {
		when(exchangeRateReadPort.findLatestDistinctByPair())
				.thenReturn(List.of(new ExchangeRateSnapshot("USD/BRL", new BigDecimal("5"), TS, RateSource.API)));
		String body = "{\"from\":\"USD\",\"to\":\"BRL\",\"amount\":2}";
		mockMvc.perform(post("/convert").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(10));
	}

	@Test
	void returns404WhenNoPath() throws Exception {
		when(exchangeRateReadPort.findLatestDistinctByPair()).thenReturn(List.of());
		String body = "{\"from\":\"USD\",\"to\":\"JPY\",\"amount\":1}";
		mockMvc.perform(post("/convert").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isNotFound());
	}

	@Test
	void returns400WhenAmountInvalid() throws Exception {
		String body = "{\"from\":\"USD\",\"to\":\"BRL\",\"amount\":0}";
		mockMvc.perform(post("/convert").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isBadRequest());
	}
}
