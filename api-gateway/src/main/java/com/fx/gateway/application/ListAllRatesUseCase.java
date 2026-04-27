package com.fx.gateway.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.ExchangeRateSnapshot;

/**
 * Returns a page of all stored rate snapshots. Sorting and page bounds are defined by {@link Pageable} (e.g. query
 * parameters from the web layer). When no sort is provided, the persistence adapter defaults to {@code pair} ascending.
 */
public record ListAllRatesUseCase(ExchangeRateReadPort exchangeRateReadPort) {

	public Page<ExchangeRateSnapshot> execute(Pageable pageable) {
		return exchangeRateReadPort.findAll(pageable);
	}
}
