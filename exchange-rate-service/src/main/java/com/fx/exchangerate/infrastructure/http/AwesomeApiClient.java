package com.fx.exchangerate.infrastructure.http;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private final String lastQuotesBaseUrl;

	public AwesomeApiClient(RestClient.Builder restClientBuilder,
			@Value("${fx.awesome-api.last-quotes-base-url}") String lastQuotesBaseUrl) {
		this.restClientBuilder = restClientBuilder;
		this.lastQuotesBaseUrl = lastQuotesBaseUrl;
	}

	@Override
	public Map<String, BigDecimal> fetchBidsForAwesomeHyphenPairs(List<String> awesomeHyphenPairs) {
		Map<String, BigDecimal> out = new HashMap<>();
		if (awesomeHyphenPairs == null || awesomeHyphenPairs.isEmpty()) {
			return out;
		}
		String url = lastQuotesBaseUrl + String.join(",", awesomeHyphenPairs);
		try {
			RestClient client = restClientBuilder.build();
			Map<String, Map<String, String>> body = client.get()
					.uri(url)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.body(new ParameterizedTypeReference<>() {
					});
			if (body == null) {
				return out;
			}
			for (Map.Entry<String, Map<String, String>> e : body.entrySet()) {
				Map<String, String> row = e.getValue();
				if (row == null) {
					continue;
				}
				String bid = row.get("bid");
				String code = row.get("code");
				String codein = row.get("codein");
				if (bid == null || bid.isBlank() || code == null || codein == null) {
					continue;
				}
				String display = code.trim().toUpperCase() + "/" + codein.trim().toUpperCase();
				out.put(display, new BigDecimal(bid.trim()));
			}
		}
		catch (RestClientException | ArithmeticException ex) {
			log.warn("awesome api error: {}", ex.getMessage());
		}
		return out;
	}
}
