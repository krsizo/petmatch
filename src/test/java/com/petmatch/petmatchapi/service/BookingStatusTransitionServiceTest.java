package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.exception.InvalidBookingStatusTransitionException;
import com.petmatch.petmatchapi.model.BookingStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookingStatusTransitionServiceTest {

	private final BookingStatusTransitionService service =
		new BookingStatusTransitionService();

	@Test
	void shouldAllowPendingToConfirmed() {
		service.validateTransition(BookingStatus.PENDING, BookingStatus.CONFIRMED);
	}

	@Test
	void shouldAllowPendingToCanceled() {
		service.validateTransition(BookingStatus.PENDING, BookingStatus.CANCELED);
	}

	@Test
	void shouldAllowConfirmedToCanceled() {
		service.validateTransition(BookingStatus.CONFIRMED, BookingStatus.CANCELED);
	}

	@Test
	void shouldNotAllowCanceledToConfirmed() {
		assertThatThrownBy(() ->
			service.validateTransition(BookingStatus.CANCELED, BookingStatus.CONFIRMED)
		)
			.isInstanceOf(InvalidBookingStatusTransitionException.class)
			.hasMessage("Canceled booking cannot be changed");
	}

	@Test
	void shouldNotAllowConfirmedToPending() {
		assertThatThrownBy(() ->
			service.validateTransition(BookingStatus.CONFIRMED, BookingStatus.PENDING)
		)
			.isInstanceOf(InvalidBookingStatusTransitionException.class)
			.hasMessage("Invalid booking status transition: CONFIRMED -> PENDING");
	}
}
