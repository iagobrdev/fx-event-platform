package com.fx.processing.infrastructure.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaDeadLetterConfiguration {

	@Bean
	public DefaultErrorHandler kafkaDeadLetterErrorHandler(KafkaTemplate<?, ?> kafkaTemplate,
			@Value("${fx.kafka.dlq-topic}") String dlqTopic) {
		DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate,
				(record, ex) -> new TopicPartition(dlqTopic, 0));
		return new DefaultErrorHandler(recoverer, new FixedBackOff(1000L, 2L));
	}

}
