package com.fx.exchangerate.presentation.dto;

import java.math.BigDecimal;

public record ExchangeRateStateResponse(BigDecimal lastApiRate, BigDecimal lastPublishedRate) {
}
