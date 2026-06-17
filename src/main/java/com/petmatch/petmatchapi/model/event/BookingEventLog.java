package com.petmatch.petmatchapi.model.event;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
	name = "booking_event_logs",
	indexes = {
		@Index(name = "idx_event_log_booking", columnList = "booking_id"),
		@Index(name = "idx_event_log_type", columnList = "event_type")
	}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEventLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "booking_id", nullable = false)
	private Long bookingId;

	@Column(name = "pet_id", nullable = false)
	private Long petId;

	@Column(name = "user_id", nullable = false)
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "event_type", nullable = false, length = 40)
	private BookingEventType eventType;

	@Column(name = "event_created_at", nullable = false)
	private LocalDateTime eventCreatedAt;

	@CreationTimestamp
	@Column(name = "processed_at", nullable = false, updatable = false)
	private LocalDateTime processedAt;
}
