package com.fx.processing.infrastructure.messaging;

import java.math.BigDecimal;
import java.time.Instant;

import com.fx.processing.domain.RateSource;

public record ExchangeRateEventPayload(String pair, BigDecimal rate, Instant timestamp, RateSource source) {
}
