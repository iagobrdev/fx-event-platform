package com.fx.gateway.infrastructure.persistence;

import java.util.Optional;

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
		return exchangeRateSpringRepository.findTopByPairOrderByTimestampDesc(pair)
				.map(doc -> new ExchangeRateSnapshot(doc.pair(), doc.rate(), doc.timestamp(), doc.source()));
	}
}
