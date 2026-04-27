package com.fx.gateway.application;

import java.math.BigDecimal;
import java.util.Optional;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.CurrencyBridge;
import com.fx.gateway.domain.CurrencyCode;

/**
 * Converts an amount between two currencies using the latest known rates as edges in a directed graph.
 *
 * <p>Rules:
 * <ul>
 *   <li>{@code amount} must be strictly positive; {@code null}, zero, or negative values throw
 *       {@link IllegalArgumentException}.</li>
 *   <li>Same currency ({@code from} equals {@code to}): returns the amount with normalized scale only (no edge required).</li>
 *   <li>Different currencies: uses {@link CurrencyBridge} over all distinct pairs from persistence; finds a path via
 *       cross rates (BFS). If no path exists, returns {@link Optional#empty()} (HTTP maps to 404).</li>
 *   <li>Each stored pair contributes a forward edge (base→quote) and a reverse edge (quote→base) with reciprocal factor.</li>
 * </ul>
 */
public record ConvertCurrencyUseCase(ExchangeRateReadPort exchangeRateReadPort) {

	/**
	 * @param from source currency
	 * @param to target currency
	 * @param amount strictly positive amount in the source currency
	 * @return converted amount in the target currency, or empty if no rate path exists
	 * @throws IllegalArgumentException if {@code amount} is invalid
	 */
	public Optional<BigDecimal> execute(CurrencyCode from, CurrencyCode to, BigDecimal amount) {
		if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("amount must be positive");
		}
		return CurrencyBridge.convert(from, to, amount, exchangeRateReadPort.findLatestDistinctByPair());
	}
}
