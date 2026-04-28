package com.fx.gateway.application.port.out;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fx.gateway.domain.ExchangeRateSnapshot;

public interface ExchangeRateReadPort {

	Optional<ExchangeRateSnapshot> findLatestByPair(String pair);

	List<ExchangeRateSnapshot> findLatestDistinctByPair();

	Page<ExchangeRateSnapshot> findAll(Pageable pageable);
}
