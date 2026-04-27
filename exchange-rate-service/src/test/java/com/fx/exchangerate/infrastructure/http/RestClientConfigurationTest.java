package com.fx.exchangerate.infrastructure.http;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

@SpringBootTest(classes = RestClientConfiguration.class)
class RestClientConfigurationTest {

	@Autowired
	private RestClient.Builder restClientBuilder;

	@Test
	void providesBuilder() {
		assertThat(restClientBuilder).isNotNull();
	}
}
