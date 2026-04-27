package com.fx.gateway.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fx.gateway.presentation.dto.ConvertRequest;

import jakarta.validation.Valid;

class ApiExceptionHandlerTest {

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(new ThrowingController(), new ValidationProbeController())
				.setControllerAdvice(new ApiExceptionHandler()).build();
	}

	@Test
	void mapsIllegalArgumentTo400() throws Exception {
		mockMvc.perform(get("/throw-ia")).andExpect(status().isBadRequest());
	}

	@Test
	void mapsValidationFailureTo400() throws Exception {
		mockMvc.perform(post("/valid-body").contentType(MediaType.APPLICATION_JSON).content("{\"from\":\"\",\"to\":\"BRL\",\"amount\":1}"))
				.andExpect(status().isBadRequest());
	}

	@RestController
	static class ThrowingController {

		@GetMapping("/throw-ia")
		void boom() {
			throw new IllegalArgumentException("bad");
		}
	}

	@RestController
	static class ValidationProbeController {

		@PostMapping("/valid-body")
		void post(@Valid @RequestBody ConvertRequest req) {
		}
	}
}
