package com.fx.processing.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExchangeRateSpringRepository extends MongoRepository<ExchangeRateDocument, String> {
}
