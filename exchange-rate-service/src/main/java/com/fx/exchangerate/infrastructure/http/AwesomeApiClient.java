package com.fx.exchangerate.infrastructure.http;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.fx.exchangerate.application.port.out.AwesomeFxRatesPort;

@Component
public class AwesomeApiClient implements AwesomeFxRatesPort {

	private static final Logger log = LoggerFactory.getLogger(AwesomeApiClient.class);

	private final RestClient.Builder restClientBuilder;

	private final String usdBrlUrl;

	public AwesomeApiClient(RestClient.Builder restClientBuilder,
			@Value("${fx.awesome-api.usd-brl-url}") String usdBrlUrl) {
		this.restClientBuilder = restClientBuilder;
		this.usdBrlUrl = usdBrlUrl;
	}

	@Override
	public Optional<BigDecimal> fetchUsdBrlBid() {
		try {
			RestClient client = restClientBuilder.build();
			Map<String, Map<String, String>> body = client.get()
					.uri(usdBrlUrl)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.body(new ParameterizedTypeReference<Map<String, Map<String, String>>>() {
					});
			if (body == null || !body.containsKey("USDBRL")) {
				log.warn("unexpected awesome api payload");
				return Optional.empty();
			}
			Map<String, String> pair = body.get("USDBRL");
			String bid = pair.get("bid");
			if (bid == null || bid.isBlank()) {
				log.warn("missing bid in awesome api payload");
				return Optional.empty();
			}
			return Optional.of(new BigDecimal(bid.trim()));
		}
		catch (RestClientException | ArithmeticException ex) {
			log.warn("awesome api error: {}", ex.getMessage());
			return Optional.empty();
		}
	}
}
