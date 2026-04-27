package com.fx.processing.infrastructure.messaging;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.processing.application.ProcessExchangeRateUseCase;
import com.fx.processing.application.port.out.ExchangeRateStorePort;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerAdapterTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Mock
	private ExchangeRateStorePort exchangeRateStorePort;

	private KafkaConsumerAdapter adapter;

	@BeforeEach
	void setUp() {
		ProcessExchangeRateUseCase processExchangeRateUseCase = new ProcessExchangeRateUseCase(exchangeRateStorePort);
		ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
		adapter = new KafkaConsumerAdapter(objectMapper, processExchangeRateUseCase);
	}

	@Test
	void invokesUseCaseForValidJson() {
		String json = "{\"pair\":\"USD/BRL\",\"rate\":5,\"timestamp\":\"" + TS + "\",\"source\":\"API\"}";
		adapter.onMessage(json);
		verify(exchangeRateStorePort).upsert(any());
	}

	@Test
	void ignoresMalformedJson() {
		adapter.onMessage("{");
		verify(exchangeRateStorePort, never()).upsert(any());
	}

	@Test
	void usesApiWhenSourceNull() {
		String json = "{\"pair\":\"USD/BRL\",\"rate\":5,\"timestamp\":\"" + TS + "\",\"source\":null}";
		adapter.onMessage(json);
		verify(exchangeRateStorePort).upsert(any());
	}

	@Test
	void skipsInvalidDomainPayload() {
		String json = "{\"pair\":\"INVALID\",\"rate\":5,\"timestamp\":\"" + TS + "\",\"source\":\"API\"}";
		adapter.onMessage(json);
		verify(exchangeRateStorePort, never()).upsert(any());
	}
}
