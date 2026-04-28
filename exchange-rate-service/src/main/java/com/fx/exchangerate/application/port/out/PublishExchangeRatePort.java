package com.fx.exchangerate.application.port.out;

import com.fx.exchangerate.domain.ExchangeRate;

public interface PublishExchangeRatePort {

	void publish(ExchangeRate exchangeRate);
}
