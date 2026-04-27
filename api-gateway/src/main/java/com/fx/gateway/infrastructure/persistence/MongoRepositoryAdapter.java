package com.fx.gateway.infrastructure.persistence;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Component;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;
import com.fx.gateway.domain.ExchangeRateSnapshot;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MongoRepositoryAdapter implements ExchangeRateReadPort {

	private final ExchangeRateSpringRepository exchangeRateSpringRepository;

	private final MongoTemplate mongoTemplate;

	@Override
	public Optional<ExchangeRateSnapshot> findLatestByPair(String pair) {
		return exchangeRateSpringRepository.findTopByPairOrderByTimestampDesc(pair)
				.map(doc -> new ExchangeRateSnapshot(doc.pair(), doc.rate(), doc.timestamp(), doc.source()));
	}

	@Override
	public List<ExchangeRateSnapshot> findLatestDistinctByPair() {
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.sort(Sort.Direction.DESC, "timestamp"),
				Aggregation.group("pair").first(Aggregation.ROOT).as("doc"), Aggregation.replaceRoot("doc"));
		AggregationResults<ExchangeRateDocument> results = mongoTemplate.aggregate(aggregation, "exchange_rates",
				ExchangeRateDocument.class);
		return results.getMappedResults().stream()
				.map(doc -> new ExchangeRateSnapshot(doc.pair(), doc.rate(), doc.timestamp(), doc.source()))
				.toList();
	}
}
