package com.petmatch.petmatchapi.dto.event;

import com.petmatch.petmatchapi.model.event.BookingEventType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class BookingEventLogResponse {
	private Long id;
	private Long bookingId;
	private Long petId;
	private Long userId;
	private BookingEventType eventType;
	private LocalDateTime eventCreatedAt;
	private LocalDateTime processedAt;
}
