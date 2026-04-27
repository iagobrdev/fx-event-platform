package com.fx.processing.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.processing.application.ProcessExchangeRateUseCase;
import com.fx.processing.domain.CurrencyPair;
import com.fx.processing.domain.ExchangeRate;
import com.fx.processing.domain.RateSource;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KafkaConsumerAdapter {

	private static final Logger log = LoggerFactory.getLogger(KafkaConsumerAdapter.class);

	private final ObjectMapper objectMapper;

	private final ProcessExchangeRateUseCase processExchangeRateUseCase;

	@KafkaListener(topics = "${fx.kafka.topic}", groupId = "${spring.kafka.consumer.group-id}")
	public void onMessage(String payload) {
		try {
			ExchangeRateEventPayload event = objectMapper.readValue(payload, ExchangeRateEventPayload.class);
			ExchangeRate rate = ExchangeRate.restore(CurrencyPair.of(event.pair()), event.rate(), event.timestamp(),
					event.source() != null ? event.source() : RateSource.API);
			processExchangeRateUseCase.execute(rate);
		}
		catch (JsonProcessingException ex) {
			log.error("invalid kafka payload", ex);
			throw new IllegalStateException("invalid kafka payload", ex);
		}
		catch (IllegalArgumentException ex) {
			log.warn("rejected event: {}", ex.getMessage());
			throw ex;
		}
	}
}
