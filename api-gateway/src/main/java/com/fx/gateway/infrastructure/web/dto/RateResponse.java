package com.fx.gateway.infrastructure.web.dto;

import java.math.BigDecimal;
import java.time.Instant;

import com.fx.gateway.domain.RateSource;

public record RateResponse(String pair, BigDecimal rate, Instant timestamp, RateSource source) {
}
