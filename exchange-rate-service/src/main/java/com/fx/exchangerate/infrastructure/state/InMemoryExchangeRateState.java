package com.fx.exchangerate.infrastructure.state;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;

@Component
public class InMemoryExchangeRateState implements ExchangeRateStatePort {

	private final ConcurrentHashMap<String, BigDecimal> lastApi = new ConcurrentHashMap<>();

	private final ConcurrentHashMap<String, BigDecimal> lastPublished = new ConcurrentHashMap<>();

	@Override
	public void setLastApiRate(String pairDisplay, BigDecimal rate) {
		lastApi.put(pairDisplay, rate);
	}

	@Override
	public void setLastPublishedRate(String pairDisplay, BigDecimal rate) {
		lastPublished.put(pairDisplay, rate);
	}

	@Override
	public Optional<BigDecimal> getLastApiRate(String pairDisplay) {
		return Optional.ofNullable(lastApi.get(pairDisplay));
	}

	@Override
	public Optional<BigDecimal> getLastPublishedRate(String pairDisplay) {
		return Optional.ofNullable(lastPublished.get(pairDisplay));
	}

	@Override
	public Set<String> allTrackedDisplayPairs() {
		Set<String> u = new HashSet<>(lastApi.keySet());
		u.addAll(lastPublished.keySet());
		return u;
	}
}
