package com.fx.gateway.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExchangeRateSpringRepository extends MongoRepository<ExchangeRateDocument, String> {

	Optional<ExchangeRateDocument> findTopByPairOrderByTimestampDesc(String pair);
}
