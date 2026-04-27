package com.fx.processing.infrastructure.persistence;

import org.springframework.stereotype.Component;

import com.fx.processing.application.port.out.ExchangeRateStorePort;
import com.fx.processing.domain.ExchangeRate;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MongoRepositoryAdapter implements ExchangeRateStorePort {

	private final ExchangeRateSpringRepository exchangeRateSpringRepository;

	@Override
	public void upsert(ExchangeRate exchangeRate) {
		ExchangeRateDocument document = new ExchangeRateDocument(exchangeRate.pair().value(), exchangeRate.rate(),
				exchangeRate.timestamp(), exchangeRate.source());
		exchangeRateSpringRepository.save(document);
	}
}
