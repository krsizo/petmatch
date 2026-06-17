package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.config.MatchScoreWeights;
import com.petmatch.petmatchapi.dto.match.PetMatchResponse;
import com.petmatch.petmatchapi.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PetScoringServiceTest {

	private PetScoringService petScoringService;

	@BeforeEach
	void setUp() {
		petScoringService = new PetScoringService(new MatchScoreWeights());
	}

	@Test
	void shouldCalculateFullMatchScore() {
		Pet pet = Pet.builder()
			.id(1L)
			.name("Buddy")
			.age(3)
			.breed("Labrador Retriever")
			.gender(Gender.MALE)
			.size(Size.MEDIUM)
			.temperament("Friendly and playful")
			.petType(PetType.DOG)
			.location("Tallinn")
			.latitude(59.4370)
			.longitude(24.7536)
			.adoptionFee(BigDecimal.valueOf(50))
			.build();

		UserPreference preference = UserPreference.builder()
			.preferredPetType(PetType.DOG)
			.preferredGender(Gender.MALE)
			.preferredSize(Size.MEDIUM)
			.minAge(1)
			.maxAge(5)
			.maxAdoptionFee(BigDecimal.valueOf(100))
			.preferredTemperament("friendly")
			.preferredLatitude(59.4370)
			.preferredLongitude(24.7536)
			.searchRadiusKm(10.0)
			.build();

		PetMatchResponse result = petScoringService.calculateMatch(pet, preference);

		assertThat(result.getScore()).isEqualTo(100);
		assertThat(result.getReasons()).hasSize(7);

		assertThat(result.getReasons())
			.extracting("criterion")
			.containsExactlyInAnyOrder(
				"pet_type",
				"gender",
				"size",
				"age",
				"budget",
				"temperament",
				"distance"
			);
	}

	@Test
	void shouldReturnZeroScoreWhenNothingMatches() {
		Pet pet = Pet.builder()
			.id(1L)
			.name("Mittens")
			.age(10)
			.breed("Siamese")
			.gender(Gender.FEMALE)
			.size(Size.SMALL)
			.temperament("Calm")
			.petType(PetType.CAT)
			.location("Helsinki")
			.latitude(60.1699)
			.longitude(24.9384)
			.adoptionFee(BigDecimal.valueOf(200))
			.build();

		UserPreference preference = UserPreference.builder()
			.preferredPetType(PetType.DOG)
			.preferredGender(Gender.MALE)
			.preferredSize(Size.LARGE)
			.minAge(1)
			.maxAge(3)
			.maxAdoptionFee(BigDecimal.valueOf(50))
			.preferredTemperament("playful")
			.preferredLatitude(59.4370)
			.preferredLongitude(24.7536)
			.searchRadiusKm(5.0)
			.build();

		PetMatchResponse result = petScoringService.calculateMatch(pet, preference);

		assertThat(result.getScore()).isZero();
		assertThat(result.getReasons()).isEmpty();
	}

	@Test
	void shouldMatchOnlyAgeWhenPetWithinPreferredAgeRange() {
		Pet pet = Pet.builder()
			.id(1L)
			.name("Charlie")
			.age(4)
			.petType(PetType.DOG)
			.build();

		UserPreference preference = UserPreference.builder()
			.minAge(3)
			.maxAge(5)
			.build();

		PetMatchResponse result = petScoringService.calculateMatch(pet, preference);

		assertThat(result.getScore()).isEqualTo(15);
		assertThat(result.getReasons()).hasSize(1);
		assertThat(result.getReasons().get(0).getCriterion()).isEqualTo("age");
	}

	@Test
	void shouldNotAddDistanceScoreWhenCoordinatesAreMissing() {
		Pet pet = Pet.builder()
			.id(1L)
			.name("Buddy")
			.petType(PetType.DOG)
			.latitude(null)
			.longitude(null)
			.build();

		UserPreference preference = UserPreference.builder()
			.preferredLatitude(59.4370)
			.preferredLongitude(24.7536)
			.searchRadiusKm(10.0)
			.build();

		PetMatchResponse result = petScoringService.calculateMatch(pet, preference);

		assertThat(result.getScore()).isZero();
		assertThat(result.getReasons()).isEmpty();
	}
}
