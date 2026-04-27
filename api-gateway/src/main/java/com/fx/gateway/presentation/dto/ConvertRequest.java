package com.fx.gateway.presentation.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ConvertRequest(
		@NotBlank String from,
		@NotBlank String to,
		@NotNull @Positive BigDecimal amount) {
}
