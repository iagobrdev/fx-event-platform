package com.fx.processing.infrastructure.persistence;

import java.time.Instant;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExchangeRateSpringRepository extends MongoRepository<ExchangeRateDocument, String> {

	boolean existsByPairAndTimestamp(String pair, Instant timestamp);
}
