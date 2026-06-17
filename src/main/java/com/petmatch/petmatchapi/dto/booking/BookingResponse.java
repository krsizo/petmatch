package com.petmatch.petmatchapi.dto.booking;

import com.petmatch.petmatchapi.model.BookingStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
	private Long id;
	private Long petId;
	private String petName;
	private Long userId;
	private String userName;
	private LocalDateTime appointmentTime;
	private BookingStatus status;
}
