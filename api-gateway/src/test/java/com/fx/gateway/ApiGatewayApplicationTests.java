package com.fx.gateway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fx.gateway.application.port.out.ExchangeRateReadPort;

@SpringBootTest
class ApiGatewayApplicationTests {

	@MockBean
	private ExchangeRateReadPort exchangeRateReadPort;

	@Test
	void contextLoads() {
	}
}
