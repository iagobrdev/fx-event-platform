package com.fx.processing.infrastructure.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import com.fx.processing.application.port.out.ExchangeRateStorePort;
import com.fx.processing.domain.CurrencyPair;
import com.fx.processing.domain.ExchangeRate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MongoRepositoryAdapter implements ExchangeRateStorePort {

	private final ExchangeRateSpringRepository exchangeRateSpringRepository;

	@Override
	public boolean existsByPairAndTimestamp(CurrencyPair pair, Instant timestamp) {
		return exchangeRateSpringRepository.existsByPairAndTimestamp(pair.value(), timestamp);
	}

	@Override
	public void save(ExchangeRate exchangeRate) {
		ExchangeRateDocument document = new ExchangeRateDocument(UUID.randomUUID().toString(), exchangeRate.pair().value(),
				exchangeRate.rate(), exchangeRate.timestamp(), exchangeRate.source());
		try {
			exchangeRateSpringRepository.save(document);
		}
		catch (DuplicateKeyException ex) {
			log.debug("mongo duplicate key for pair {} at {}", exchangeRate.pair().value(), exchangeRate.timestamp());
		}
	}
}
