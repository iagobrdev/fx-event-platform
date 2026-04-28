package com.fx.exchangerate.application.port.out;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

public interface ExchangeRateStatePort {

	void setLastApiRate(String pairDisplay, BigDecimal rate);

	void setLastPublishedRate(String pairDisplay, BigDecimal rate);

	Optional<BigDecimal> getLastApiRate(String pairDisplay);

	Optional<BigDecimal> getLastPublishedRate(String pairDisplay);

	Set<String> allTrackedDisplayPairs();
}
