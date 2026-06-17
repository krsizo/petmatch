package com.petmatch.petmatchapi.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BookingRequest {
	@NotNull
	private Long petId;

	@NotNull
	@Future(message = "Appointment time must be in the future")
	private LocalDateTime appointmentTime;
}
