package com.fx.gateway.application;

import java.util.Optional;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.ExchangeRateSnapshot;
import com.fx.gateway.domain.ParsedCurrencyPair;

/**
 * Retrieves the latest persisted exchange-rate snapshot for a currency pair (e.g. from MongoDB).
 *
 * <p>Rules:
 * <ul>
 *   <li>The pair string must be {@code BASE/QUOTE} (e.g. {@code USD/BRL}) with valid ISO 4217 3-letter codes;
 *       otherwise {@link IllegalArgumentException} is thrown.</li>
 *   <li>Lookup uses the canonical display form produced after parsing.</li>
 *   <li>If no record exists for the pair, returns {@link Optional#empty()} (HTTP layer maps this to 404).</li>
 * </ul>
 */
public record GetLatestRateUseCase(ExchangeRateReadPort exchangeRateReadPort) {

	/**
	 * @param pair currency pair in {@code BASE/QUOTE} form
	 * @return latest known snapshot for the pair, or empty if none
	 * @throws IllegalArgumentException if the pair string or currency codes are invalid
	 */
	public Optional<ExchangeRateSnapshot> execute(String pair) {
		ParsedCurrencyPair parsed = ParsedCurrencyPair.parse(pair);
		return exchangeRateReadPort.findLatestByPair(parsed.display());
	}
}
