package com.fx.gateway.presentation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fx.gateway.application.ConvertCurrencyUseCase;
import com.fx.gateway.domain.CurrencyCode;
import com.fx.gateway.presentation.dto.ConvertRequest;
import com.fx.gateway.presentation.dto.ConvertResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ConversionController {

	private final ConvertCurrencyUseCase convertCurrencyUseCase;

	@PostMapping("/convert")
	public ResponseEntity<ConvertResponse> convert(@Valid @RequestBody ConvertRequest request) {
		return convertCurrencyUseCase
				.execute(CurrencyCode.of(request.from()), CurrencyCode.of(request.to()), request.amount())
				.map(ConvertResponse::new)
				.map(ResponseEntity::ok)
				.orElseGet(() -> {
					log.warn("convert failed: no rate path for {} {}", request.from(), request.to());
					return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
				});
	}
}
