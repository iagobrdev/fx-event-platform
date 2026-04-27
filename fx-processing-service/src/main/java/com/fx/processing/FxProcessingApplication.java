package com.fx.processing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class FxProcessingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxProcessingApplication.class, args);
	}
}
