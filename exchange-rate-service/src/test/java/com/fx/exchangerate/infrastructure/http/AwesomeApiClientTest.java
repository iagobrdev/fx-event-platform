package com.fx.exchangerate.infrastructure.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class AwesomeApiClientTest {

	private MockRestServiceServer server;

	private AwesomeApiClient client;

	@BeforeEach
	void setUp() {
		RestClient.Builder builder = RestClient.builder();
		server = MockRestServiceServer.bindTo(builder).build();
		client = new AwesomeApiClient(builder, "http://localhost/last/");
	}

	@AfterEach
	void tearDown() {
		server.verify();
	}

	@Test
	void returnsEmptyForEmptyInput() {
		assertThat(client.fetchBidsForAwesomeHyphenPairs(List.of())).isEmpty();
		assertThat(client.fetchBidsForAwesomeHyphenPairs(null)).isEmpty();
	}

	@Test
	void parsesBidRows() {
		String json = "{\"USDBRL\":{\"bid\":\"5.12\",\"code\":\"USD\",\"codein\":\"BRL\"}}";
		server.expect(requestTo("http://localhost/last/USd-BRL")).andRespond(withSuccess(json, MediaType.APPLICATION_JSON));
		Map<String, BigDecimal> out = client.fetchBidsForAwesomeHyphenPairs(List.of("USd-BRL"));
		assertThat(out).containsEntry("USD/BRL", new BigDecimal("5.12"));
	}

	@Test
	void returnsEmptyWhenBodyHasNoRows() {
		server.expect(requestTo("http://localhost/last/USD-BRL")).andRespond(withSuccess("{}", MediaType.APPLICATION_JSON));
		assertThat(client.fetchBidsForAwesomeHyphenPairs(List.of("USD-BRL"))).isEmpty();
	}
}
