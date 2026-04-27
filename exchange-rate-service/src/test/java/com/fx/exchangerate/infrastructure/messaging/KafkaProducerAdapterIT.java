package com.fx.exchangerate.infrastructure.messaging;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fx.exchangerate.domain.CurrencyPair;
import com.fx.exchangerate.domain.ExchangeRate;
import com.fx.exchangerate.domain.RateSource;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

@SpringBootTest(classes = { KafkaProducerAdapter.class, KafkaProducerAdapterIT.KafkaProducerItConfig.class }, properties = {
		"fx.kafka.topic=exchange-rate-events", "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}" })
@EmbeddedKafka(partitions = 1, topics = { "exchange-rate-events" }, bootstrapServersProperty = "spring.embedded.kafka.brokers")
class KafkaProducerAdapterIT {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Autowired
	private KafkaProducerAdapter kafkaProducerAdapter;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;

	@Test
	void publishesJsonMessage() {
		kafkaProducerAdapter.publish(ExchangeRate.create(CurrencyPair.of("USD/BRL"), new BigDecimal("5"), TS, RateSource.API));
		Map<String, Object> consumerProps = new HashMap<>(KafkaTestUtils.consumerProps("it-group", "true", embeddedKafkaBroker));
		consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps)) {
			consumer.subscribe(List.of("exchange-rate-events"));
			ConsumerRecords<String, String> records = KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(10));
			assertThat(records.count()).isGreaterThanOrEqualTo(1);
			ConsumerRecord<String, String> last = records.iterator().next();
			assertThat(last.key()).isEqualTo("USD/BRL");
			assertThat(last.value()).contains("USD/BRL");
		}
	}

	@Configuration
	static class KafkaProducerItConfig {

		@Bean
		KafkaTemplate<String, String> kafkaTemplate(EmbeddedKafkaBroker broker) {
			Map<String, Object> props = new HashMap<>();
			props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker.getBrokersAsString());
			props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
			return new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(props));
		}

		@Bean
		ObjectMapper objectMapper() {
			return new ObjectMapper().findAndRegisterModules();
		}
	}
}
