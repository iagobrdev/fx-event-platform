package com.fx.exchangerate.presentation;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.presentation.dto.ExchangeRateStateResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {

	private final ExchangeRateStatePort exchangeRateStatePort;

	@GetMapping("/state")
	public ExchangeRateStateResponse state() {
		return new ExchangeRateStateResponse(exchangeRateStatePort.getLastApiRate().orElse(null),
				exchangeRateStatePort.getLastPublishedRate().orElse(null));
	}
}
