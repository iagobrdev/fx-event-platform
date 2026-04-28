package com.fx.processing.application.port.out;

import com.fx.processing.domain.ExchangeRate;

public interface ExchangeRateStorePort {

	void upsert(ExchangeRate exchangeRate);
}
