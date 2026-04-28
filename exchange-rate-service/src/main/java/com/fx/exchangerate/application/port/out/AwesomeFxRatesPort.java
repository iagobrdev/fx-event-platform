package com.fx.exchangerate.application.port.out;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AwesomeFxRatesPort {

	Map<String, BigDecimal> fetchBidsForAwesomeHyphenPairs(List<String> awesomeHyphenPairs);
}
