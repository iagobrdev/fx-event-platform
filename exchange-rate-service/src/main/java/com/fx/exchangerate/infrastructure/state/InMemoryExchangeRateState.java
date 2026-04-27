package com.fx.exchangerate.infrastructure.state;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;

@Component
public class InMemoryExchangeRateState implements ExchangeRateStatePort {

	private final AtomicReference<BigDecimal> lastApi = new AtomicReference<>();
	private final AtomicReference<BigDecimal> lastPublished = new AtomicReference<>();

	@Override
	public void setLastApiRate(BigDecimal rate) {
		lastApi.set(rate);
	}

	@Override
	public void setLastPublishedRate(BigDecimal rate) {
		lastPublished.set(rate);
	}

	@Override
	public Optional<BigDecimal> getLastApiRate() {
		return Optional.ofNullable(lastApi.get());
	}

	@Override
	public Optional<BigDecimal> getLastPublishedRate() {
		return Optional.ofNullable(lastPublished.get());
	}
}
