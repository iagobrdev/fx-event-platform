package com.fx.exchangerate.infrastructure.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.exchangerate.application.port.out.PublishExchangeRatePort;
import com.fx.exchangerate.domain.ExchangeRate;

@Component
public class KafkaProducerAdapter implements PublishExchangeRatePort {

	private static final Logger log = LoggerFactory.getLogger(KafkaProducerAdapter.class);

	private final KafkaTemplate<String, String> kafkaTemplate;

	private final ObjectMapper objectMapper;

	private final String topic;

	public KafkaProducerAdapter(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper,
			@Value("${fx.kafka.topic}") String topic) {
		this.kafkaTemplate = kafkaTemplate;
		this.objectMapper = objectMapper;
		this.topic = topic;
	}

	@Override
	public void publish(ExchangeRate exchangeRate) {
		try {
			ExchangeRateEventPayload payload = ExchangeRateEventPayload.from(exchangeRate);
			String json = objectMapper.writeValueAsString(payload);
			kafkaTemplate.send(topic, exchangeRate.pair().value(), json);
		}
		catch (JsonProcessingException ex) {
			log.error("failed to serialize exchange rate event", ex);
		}
	}
}
