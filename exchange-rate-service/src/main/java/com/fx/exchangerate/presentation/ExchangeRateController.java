package com.fx.exchangerate.presentation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fx.exchangerate.application.port.out.ExchangeRateStatePort;
import com.fx.exchangerate.presentation.dto.ExchangeRateStateResponse;
import com.fx.exchangerate.presentation.dto.ExchangeRateStateResponse.PairRateCacheView;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/exchange-rate")
@RequiredArgsConstructor
public class ExchangeRateController {

	private final ExchangeRateStatePort exchangeRateStatePort;

	@GetMapping("/state")
	public ExchangeRateStateResponse state() {
		Map<String, PairRateCacheView> pairs = new LinkedHashMap<>();
		for (String display : exchangeRateStatePort.allTrackedDisplayPairs().stream().sorted().toList()) {
			pairs.put(display, new PairRateCacheView(exchangeRateStatePort.getLastApiRate(display).orElse(null),
					exchangeRateStatePort.getLastPublishedRate(display).orElse(null)));
		}
		return new ExchangeRateStateResponse(pairs);
	}
}
