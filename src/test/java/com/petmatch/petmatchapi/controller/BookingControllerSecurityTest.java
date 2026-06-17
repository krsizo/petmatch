package com.petmatch.petmatchapi.controller;

import com.petmatch.petmatchapi.config.JwtAuthFilter;
import com.petmatch.petmatchapi.mapper.BookingEventLogMapper;
import com.petmatch.petmatchapi.mapper.BookingMapper;
import com.petmatch.petmatchapi.service.BookingEventLogService;
import com.petmatch.petmatchapi.service.BookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerSecurityTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private BookingService bookingService;

	@MockitoBean
	private BookingMapper bookingMapper;

	@MockitoBean
	private BookingEventLogService bookingEventLogService;

	@MockitoBean
	private BookingEventLogMapper bookingEventLogMapper;

	@MockitoBean
	private JwtAuthFilter jwtAuthFilter;

	@Test
	void shouldRejectCreateBookingWithoutToken() throws Exception {
		mockMvc.perform(post("/api/bookings")
				.contentType("application/json")
				.content("""
					{
					  "petId": 1,
					  "appointmentTime": "2026-06-10T12:00:00"
					}
					"""))
			.andExpect(status().isForbidden());
	}
}
