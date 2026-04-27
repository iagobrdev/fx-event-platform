package com.fx.exchangerate.infrastructure.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fx.exchangerate.application.FetchExchangeRateUseCase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ExchangeRateSchedule {

	private final FetchExchangeRateUseCase fetchExchangeRateUseCase;

	@Scheduled(fixedRateString = "${fx.poll.api-interval-ms}")
	public void pollApi() {
		fetchExchangeRateUseCase.execute();
	}
}
