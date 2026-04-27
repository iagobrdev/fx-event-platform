package com.fx.gateway.infrastructure.persistence;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Component;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.ExchangeRateSnapshot;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MongoRepositoryAdapter implements ExchangeRateReadPort {

	private final ExchangeRateSpringRepository exchangeRateSpringRepository;

	@Override
	public Optional<ExchangeRateSnapshot> findLatestByPair(String pair) {
		return exchangeRateSpringRepository.findById(pair)
				.map(doc -> new ExchangeRateSnapshot(doc.pair(), doc.rate(), doc.timestamp(), doc.source()));
	}

	@Override
	public List<ExchangeRateSnapshot> findLatestDistinctByPair() {
		return StreamSupport.stream(exchangeRateSpringRepository.findAll().spliterator(), false)
				.map(doc -> new ExchangeRateSnapshot(doc.pair(), doc.rate(), doc.timestamp(), doc.source()))
				.toList();
	}
}
