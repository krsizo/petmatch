package com.petmatch.petmatchapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(
	name = "bookings",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_booking_pet_time", columnNames = {"pet_id", "appointment_time"})
	},
	indexes = {
		@Index(name = "idx_booking_user", columnList = "user_id"),
		@Index(name = "idx_booking_pet", columnList = "pet_id")
	}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "pet_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Pet pet;

	@Column(name = "appointment_time", nullable = false)
	private LocalDateTime appointmentTime;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 12)
	@Builder.Default
	private BookingStatus status = BookingStatus.PENDING;

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;
}
