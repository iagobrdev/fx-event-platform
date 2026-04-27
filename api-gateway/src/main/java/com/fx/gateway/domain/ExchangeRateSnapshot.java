package com.fx.gateway.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record ExchangeRateSnapshot(String pair, BigDecimal rate, Instant timestamp, RateSource source) {
}
