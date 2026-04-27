package com.fx.gateway.infrastructure.persistence;

import java.util.List;
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
		return exchangeRateSpringRepository.findById(pair)
				.map(doc -> new ExchangeRateSnapshot(pairLabel(doc), doc.rate(), doc.timestamp(), doc.source()));
	}

	@Override
	public List<ExchangeRateSnapshot> findLatestDistinctByPair() {
		return exchangeRateSpringRepository.findAll().stream()
				.map(doc -> new ExchangeRateSnapshot(pairLabel(doc), doc.rate(), doc.timestamp(), doc.source()))
				.toList();
	}

	private static String pairLabel(ExchangeRateDocument doc) {
		String p = doc.pair();
		return p != null && !p.isBlank() ? p : doc.id();
	}
}
