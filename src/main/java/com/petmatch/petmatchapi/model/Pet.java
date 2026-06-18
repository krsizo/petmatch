package com.petmatch.petmatchapi.model;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	@Column(nullable = false)
	private long version;

	@Column(nullable = false, length = 80)
	private String name;

	@Column(nullable = false)
	private Integer age;

	@Column(nullable = false, length = 80)
	private String breed;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private Gender gender;

	@Column(nullable = false, length = 10)
	@Enumerated(EnumType.STRING)
	private Size size;

	@Column(length = 60)
	private String temperament;

	@Column(length = 1000)
	private String description;

	@Column(length = 500)
	private String imageUrl;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 12)
	@Builder.Default
	private PetStatus status = PetStatus.AVAILABLE;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 10)
	private PetType petType;

	@Column(nullable = false, length = 80)
	private String location;

	private Double latitude;

	private Double longitude;

	@Column(precision = 10, scale = 2)
	private BigDecimal adoptionFee;
}
