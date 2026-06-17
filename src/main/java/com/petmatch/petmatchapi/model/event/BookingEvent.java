package com.petmatch.petmatchapi.model.event;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingEvent {
	private Long bookingId;
	private Long petId;
	private Long userId;
	private BookingEventType eventType;
	private LocalDateTime createdAt;
}
