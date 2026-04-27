package com.fx.gateway.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
				.map(doc -> toSnapshot(doc));
	}

	@Override
	public List<ExchangeRateSnapshot> findLatestDistinctByPair() {
		return exchangeRateSpringRepository.findAll().stream().map(MongoRepositoryAdapter::toSnapshot).toList();
	}

	@Override
	public Page<ExchangeRateSnapshot> findAll(Pageable pageable) {
		Pageable resolved = pageable.getSort().isUnsorted()
				? PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("pair").ascending())
				: pageable;
		return exchangeRateSpringRepository.findAll(resolved).map(MongoRepositoryAdapter::toSnapshot);
	}

	private static ExchangeRateSnapshot toSnapshot(ExchangeRateDocument doc) {
		return new ExchangeRateSnapshot(pairLabel(doc), doc.rate(), doc.timestamp(), doc.source(), doc.previousRate());
	}

	private static String pairLabel(ExchangeRateDocument doc) {
		String p = doc.pair();
		return p != null && !p.isBlank() ? p : doc.id();
	}
}
