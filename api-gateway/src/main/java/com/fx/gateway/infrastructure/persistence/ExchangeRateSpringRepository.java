package com.fx.gateway.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExchangeRateSpringRepository extends MongoRepository<ExchangeRateDocument, String> {
}
