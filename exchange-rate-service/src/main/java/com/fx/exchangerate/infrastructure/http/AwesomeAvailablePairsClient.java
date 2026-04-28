package com.fx.exchangerate.infrastructure.http;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.util.UriComponentsBuilder;

import com.fx.exchangerate.application.port.out.AvailableFxPairsPort;

@Component
public class AwesomeAvailablePairsClient implements AvailableFxPairsPort {

	private static final Logger log = LoggerFactory.getLogger(AwesomeAvailablePairsClient.class);

	private final RestClient.Builder restClientBuilder;

	private final String availableJsonUrl;

	private final String token;

	private final int maxPairs;

	private final Duration minRefreshInterval;

	private final AtomicReference<List<String>> cached = new AtomicReference<>(List.of());

	private final AtomicReference<Instant> lastRefresh = new AtomicReference<>(Instant.EPOCH);

	public AwesomeAvailablePairsClient(RestClient.Builder restClientBuilder,
			@Value("${fx.awesome-api.available-json-url}") String availableJsonUrl,
			@Value("${fx.awesome-api.token:}") String token,
			@Value("${fx.poll.max-pairs:500}") int maxPairs,
			@Value("${fx.poll.available-refresh-minutes:360}") long availableRefreshMinutes) {
		this.restClientBuilder = restClientBuilder;
		this.availableJsonUrl = availableJsonUrl;
		this.token = token;
		this.maxPairs = maxPairs;
		this.minRefreshInterval = Duration.ofMinutes(availableRefreshMinutes);
	}

	@PostConstruct
	void warmUp() {
		refreshIfStale();
	}

	@Override
	public List<String> awesomeHyphenPairs() {
		return cached.get();
	}

	@Override
	public void refreshIfStale() {
		Instant now = Instant.now();
		if (now.isBefore(lastRefresh.get().plus(minRefreshInterval)) && !cached.get().isEmpty()) {
			return;
		}
		try {
			RestClient client = restClientBuilder.build();
			String url = availableJsonUrl;
			if (token != null && !token.isBlank()) {
				url = UriComponentsBuilder.fromHttpUrl(availableJsonUrl).queryParam("token", token.trim()).build(true)
						.toUriString();
			}
			Map<String, String> body = client.get()
					.uri(url)
					.accept(MediaType.APPLICATION_JSON)
					.retrieve()
					.body(new ParameterizedTypeReference<Map<String, String>>() {
					});
			if (body == null || body.isEmpty()) {
				log.warn("awesome available list empty");
				return;
			}
			List<String> keys = new ArrayList<>(body.keySet());
			Collections.sort(keys);
			if (keys.size() > maxPairs) {
				keys = keys.subList(0, maxPairs);
			}
			cached.set(List.copyOf(keys));
			lastRefresh.set(now);
			log.info("awesome available pairs loaded: {}", keys.size());
		}
		catch (RestClientException ex) {
			log.warn("awesome available list error: {}", ex.getMessage());
		}
	}
}
