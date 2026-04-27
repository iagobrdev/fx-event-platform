package com.fx.exchangerate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@SpringBootTest(properties = {
		"spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
		"spring.kafka.consumer.auto-offset-reset=earliest",
		"spring.kafka.consumer.group-id=test-exchange-rate",
		"spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer",
		"spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer"
})
@EmbeddedKafka(partitions = 1, topics = { "exchange-rate-events" })
class ExchangeRateApplicationTests {

	@Test
	void contextLoads() {
	}
}
