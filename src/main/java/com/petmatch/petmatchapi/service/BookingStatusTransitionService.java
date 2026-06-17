package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.exception.InvalidBookingStatusTransitionException;
import com.petmatch.petmatchapi.model.BookingStatus;
import org.springframework.stereotype.Service;

@Service
public class BookingStatusTransitionService {

	public void validateTransition(BookingStatus current, BookingStatus target) {
		if (current == BookingStatus.CANCELED) {
			throw new InvalidBookingStatusTransitionException(
				"Canceled booking cannot be changed"
			);
		}

		boolean valid =
			current == BookingStatus.PENDING
				&& (target == BookingStatus.CONFIRMED
				|| target == BookingStatus.CANCELED)

				|| current == BookingStatus.CONFIRMED
				&& target == BookingStatus.CANCELED;

		if (!valid) {
			throw new InvalidBookingStatusTransitionException(
				"Invalid booking status transition: " + current + " -> " + target
			);
		}
	}
}
