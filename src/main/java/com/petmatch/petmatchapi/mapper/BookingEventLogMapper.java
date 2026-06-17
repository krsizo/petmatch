package com.petmatch.petmatchapi.mapper;

import com.petmatch.petmatchapi.dto.event.BookingEventLogResponse;
import com.petmatch.petmatchapi.model.event.BookingEventLog;
import org.springframework.stereotype.Component;

@Component
public class BookingEventLogMapper {

	public BookingEventLogResponse toResponse(BookingEventLog log) {
		if (log == null) {
			return null;
		}

		return BookingEventLogResponse.builder()
			.id(log.getId())
			.bookingId(log.getBookingId())
			.petId(log.getPetId())
			.userId(log.getUserId())
			.eventType(log.getEventType())
			.eventCreatedAt(log.getEventCreatedAt())
			.processedAt(log.getProcessedAt())
			.build();
	}
}
