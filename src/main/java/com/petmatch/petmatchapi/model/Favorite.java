package com.petmatch.petmatchapi.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(
	name = "favorites",
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_favorite_user_pet", columnNames = {"user_id", "pet_id"})
	},
	indexes = {
		@Index(name = "idx_fav_user", columnList = "user_id"),
		@Index(name = "idx_fav_pet", columnList = "pet_id")
	}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Favorite {

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

	@CreationTimestamp
	@Column(nullable = false, updatable = false)
	private LocalDateTime addedAt;
}
