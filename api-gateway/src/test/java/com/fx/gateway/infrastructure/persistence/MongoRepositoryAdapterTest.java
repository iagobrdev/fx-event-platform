package com.fx.gateway.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fx.gateway.domain.ExchangeRateSnapshot;
import com.fx.gateway.domain.RateSource;

@ExtendWith(MockitoExtension.class)
class MongoRepositoryAdapterTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Mock
	private ExchangeRateSpringRepository exchangeRateSpringRepository;

	@InjectMocks
	private MongoRepositoryAdapter mongoRepositoryAdapter;

	@Test
	void findLatestByPairDelegatesToRepository() {
		ExchangeRateDocument doc = new ExchangeRateDocument("USD/BRL", "USD/BRL", new BigDecimal("5"), TS, RateSource.API);
		when(exchangeRateSpringRepository.findById("USD/BRL")).thenReturn(Optional.of(doc));
		assertThat(mongoRepositoryAdapter.findLatestByPair("USD/BRL")).contains(new ExchangeRateSnapshot("USD/BRL", new BigDecimal("5"), TS, RateSource.API));
	}

	@Test
	void findLatestDistinctByPairMapsAll() {
		when(exchangeRateSpringRepository.findAll()).thenReturn(List.of(new ExchangeRateDocument("USD/BRL", "USD/BRL", new BigDecimal("5"), TS, RateSource.API)));
		assertThat(mongoRepositoryAdapter.findLatestDistinctByPair()).hasSize(1);
		verify(exchangeRateSpringRepository).findAll();
	}
}
