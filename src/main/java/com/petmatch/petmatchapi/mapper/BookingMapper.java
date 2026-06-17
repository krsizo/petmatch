package com.petmatch.petmatchapi.mapper;

import com.petmatch.petmatchapi.dto.booking.BookingResponse;
import com.petmatch.petmatchapi.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

	public BookingResponse toResponse(Booking booking) {
		if (booking == null) {
			return null;
		}

		return BookingResponse.builder()
			.id(booking.getId())
			.petId(booking.getPet().getId())
			.petName(booking.getPet().getName())
			.userId(booking.getUser().getId())
			.userName(booking.getUser().getName())
			.appointmentTime(booking.getAppointmentTime())
			.status(booking.getStatus())
			.build();
	}
}
