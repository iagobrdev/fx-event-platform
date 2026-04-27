package com.fx.gateway.infrastructure.web;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fx.gateway.application.GetLatestRateUseCase;
import com.fx.gateway.application.ListAllRatesUseCase;
import com.fx.gateway.domain.ExchangeRateSnapshot;
import com.fx.gateway.infrastructure.web.dto.RateResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RateController {

	private final GetLatestRateUseCase getLatestRateUseCase;

	private final ListAllRatesUseCase listAllRatesUseCase;

	@GetMapping("/rates/all")
	public Page<RateResponse> listAllRates(
			@PageableDefault(size = 20, sort = "pair", direction = Sort.Direction.ASC) Pageable pageable) {
		return listAllRatesUseCase.execute(pageable).map(this::toResponse);
	}

	@GetMapping("/rates")
	public ResponseEntity<RateResponse> getRate(@RequestParam("pair") String pair) {
		return getLatestRateUseCase.execute(pair)
				.map(this::toResponse)
				.map(ResponseEntity::ok)
				.orElseGet(() -> {
					log.warn("rate not found for pair {}", pair);
					return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
				});
	}

	private RateResponse toResponse(ExchangeRateSnapshot snapshot) {
		return new RateResponse(snapshot.pair(), snapshot.rate(), snapshot.timestamp(), snapshot.source(), snapshot.previousRate());
	}
}
