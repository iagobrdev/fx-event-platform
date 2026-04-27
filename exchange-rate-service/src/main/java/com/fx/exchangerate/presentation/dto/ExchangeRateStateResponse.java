package com.fx.exchangerate.presentation.dto;

import java.math.BigDecimal;
import java.util.Map;

public record ExchangeRateStateResponse(Map<String, PairRateCacheView> pairs) {

	public record PairRateCacheView(BigDecimal lastApiRate, BigDecimal lastPublishedRate) {
	}
}
