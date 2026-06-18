package com.petmatch.petmatchapi.controller;

import com.petmatch.petmatchapi.config.JwtAuthFilter;
import com.petmatch.petmatchapi.dto.auth.AuthResponse;
import com.petmatch.petmatchapi.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerApiTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private AuthService authService;

	@MockitoBean
	private JwtAuthFilter jwtAuthFilter;

	@Test
	void shouldRegisterUser() throws Exception {
		when(authService.register(any()))
			.thenReturn(new AuthResponse("fake-jwt-token"));

		mockMvc.perform(post("/api/auth/register")
				.contentType("application/json")
				.content("""
					{
					  "name": "Jane Doe",
					  "email": "jane@petmatch.com",
					  "password": "password123"
					}
					"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").value("fake-jwt-token"));
	}

	@Test
	void shouldLoginUser() throws Exception {
		when(authService.login(any()))
			.thenReturn(new AuthResponse("fake-jwt-token"));

		mockMvc.perform(post("/api/auth/login")
				.contentType("application/json")
				.content("""
					{
					  "email": "jane@petmatch.com",
					  "password": "password123"
					}
					"""))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.token").value("fake-jwt-token"));
	}

	@Test
	void shouldRejectInvalidLoginRequest() throws Exception {
		mockMvc.perform(post("/api/auth/login")
				.contentType("application/json")
				.content("""
					{
					  "email": "not-an-email",
					  "password": "123"
					}
					"""))
			.andExpect(status().isBadRequest());
	}
}
