package com.fx.exchangerate.infrastructure.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class AwesomeAvailablePairsClientTest {

	private MockRestServiceServer server;

	private AwesomeAvailablePairsClient client;

	@BeforeEach
	void setUp() {
		RestClient.Builder builder = RestClient.builder();
		server = MockRestServiceServer.bindTo(builder).build();
		client = new AwesomeAvailablePairsClient(builder, "http://localhost/available", 2, 0L);
	}

	@AfterEach
	void tearDown() {
		server.verify();
	}

	@Test
	void loadsAndTruncatesToMaxPairs() {
		String json = "{\"z-z\":\"1\",\"a-a\":\"2\",\"b-b\":\"3\"}";
		server.expect(requestTo("http://localhost/available")).andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
		client.refreshIfStale();
		assertThat(client.awesomeHyphenPairs()).containsExactly("a-a", "b-b");
	}

	@Test
	void keepsCacheEmptyOnHttpError() {
		server.expect(requestTo("http://localhost/available")).andRespond(withStatus(HttpStatus.INTERNAL_SERVER_ERROR));
		client.refreshIfStale();
		assertThat(client.awesomeHyphenPairs()).isEmpty();
	}
}
