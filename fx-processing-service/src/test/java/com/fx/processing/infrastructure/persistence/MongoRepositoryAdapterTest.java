package com.fx.processing.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fx.processing.domain.CurrencyPair;
import com.fx.processing.domain.ExchangeRate;
import com.fx.processing.domain.RateSource;

@ExtendWith(MockitoExtension.class)
class MongoRepositoryAdapterTest {

	private static final Instant TS = Instant.parse("2026-01-01T00:00:00Z");

	@Mock
	private ExchangeRateSpringRepository exchangeRateSpringRepository;

	@InjectMocks
	private MongoRepositoryAdapter adapter;

	@Test
	void upsertUsesPairAsId() {
		when(exchangeRateSpringRepository.findById("USD/BRL")).thenReturn(Optional.empty());
		ExchangeRate rate = ExchangeRate.restore(CurrencyPair.of("USD/BRL"), new BigDecimal("5"), TS, RateSource.API);
		adapter.upsert(rate);
		ArgumentCaptor<ExchangeRateDocument> cap = ArgumentCaptor.forClass(ExchangeRateDocument.class);
		verify(exchangeRateSpringRepository).save(cap.capture());
		assertThat(cap.getValue().id()).isEqualTo("USD/BRL");
		assertThat(cap.getValue().pair()).isEqualTo("USD/BRL");
		assertThat(cap.getValue().rate()).isEqualByComparingTo("5");
		assertThat(cap.getValue().previousRate()).isNull();
	}

	@Test
	void upsertSetsPreviousToLastPersistedRate() {
		when(exchangeRateSpringRepository.findById("USD/BRL"))
				.thenReturn(Optional.of(new ExchangeRateDocument("USD/BRL", "USD/BRL", new BigDecimal("4"), TS, RateSource.API, null)));
		ExchangeRate rate = ExchangeRate.restore(CurrencyPair.of("USD/BRL"), new BigDecimal("5"), TS, RateSource.API);
		adapter.upsert(rate);
		ArgumentCaptor<ExchangeRateDocument> cap = ArgumentCaptor.forClass(ExchangeRateDocument.class);
		verify(exchangeRateSpringRepository).save(cap.capture());
		assertThat(cap.getValue().rate()).isEqualByComparingTo("5");
		assertThat(cap.getValue().previousRate()).isEqualByComparingTo("4");
	}
}
