package com.fx.exchangerate.infrastructure.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fx.exchangerate.application.FetchExchangeRateUseCase;
import com.fx.exchangerate.application.SimulateExchangeRateUseCase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExchangeRateSchedule {

	private final FetchExchangeRateUseCase fetchExchangeRateUseCase;

	private final SimulateExchangeRateUseCase simulateExchangeRateUseCase;

	@Scheduled(fixedRateString = "${fx.poll.api-interval-ms}")
	public void pollApi() {
		fetchExchangeRateUseCase.execute();
	}

	@Scheduled(fixedRateString = "${fx.poll.simulation-interval-ms}")
	public void simulate() {
		simulateExchangeRateUseCase.execute();
	}
}
