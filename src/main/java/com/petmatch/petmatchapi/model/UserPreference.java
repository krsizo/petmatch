package com.petmatch.petmatchapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreference {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, unique = true)
	private User user;

	@Enumerated(EnumType.STRING)
	@Column(length = 20)
	private PetType preferredPetType;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private Gender preferredGender;

	@Enumerated(EnumType.STRING)
	@Column(length = 10)
	private Size preferredSize;

	private Integer minAge;

	private Integer maxAge;

	@Column(precision = 10, scale = 2)
	private BigDecimal maxAdoptionFee;

	@Column(length = 80)
	private String preferredLocation;

	private Double preferredLatitude;

	private Double preferredLongitude;

	private Double searchRadiusKm;

	@Column(length = 60)
	private String preferredTemperament;
}
