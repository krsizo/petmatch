package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.config.MatchScoreWeights;
import com.petmatch.petmatchapi.dto.match.MatchReason;
import com.petmatch.petmatchapi.dto.match.PetMatchResponse;
import com.petmatch.petmatchapi.model.Pet;
import com.petmatch.petmatchapi.model.UserPreference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PetScoringService {

	private final MatchScoreWeights weights;

	public PetScoringService(MatchScoreWeights weights) {
		this.weights = weights;
	}

	public PetMatchResponse calculateMatch(Pet pet, UserPreference preference) {
		int score = 0;
		List<MatchReason> reasons = new ArrayList<>();

		if (preference.getPreferredPetType() != null &&
			preference.getPreferredPetType() == pet.getPetType()) {
			score += weights.getPetType();
			reasons.add(MatchReason.builder()
				.criterion("pet_type")
				.score(weights.getPetType())
				.message("Matches preferred pet type")
				.build());
		}

		if (preference.getPreferredGender() != null &&
			preference.getPreferredGender() == pet.getGender()) {
			score += weights.getGender();
			reasons.add(MatchReason.builder()
				.criterion("gender")
				.score(weights.getGender())
				.message("Matches preferred gender")
				.build());
		}

		if (preference.getPreferredSize() != null &&
			preference.getPreferredSize() == pet.getSize()) {
			score += weights.getSize();
			reasons.add(MatchReason.builder()
				.criterion("size")
				.score(weights.getSize())
				.message("Matches preferred size")
				.build());
		}

		boolean agePreferenceProvided =
			preference.getMinAge() != null || preference.getMaxAge() != null;

		boolean matchesMinAge =
			preference.getMinAge() == null || pet.getAge() >= preference.getMinAge();

		boolean matchesMaxAge =
			preference.getMaxAge() == null || pet.getAge() <= preference.getMaxAge();

		if (agePreferenceProvided && matchesMinAge && matchesMaxAge) {
			score += weights.getAge();
			reasons.add(MatchReason.builder()
				.criterion("age")
				.score(weights.getAge())
				.message("Within preferred age range")
				.build());
		}

		if (preference.getMaxAdoptionFee() != null &&
			pet.getAdoptionFee() != null &&
			pet.getAdoptionFee().compareTo(preference.getMaxAdoptionFee()) <= 0) {
			score += weights.getAdoptionFee();
			reasons.add(MatchReason.builder()
				.criterion("budget")
				.score(weights.getAdoptionFee())
				.message("Within adoption budget")
				.build());
		}

		if (preference.getPreferredTemperament() != null &&
			pet.getTemperament() != null &&
			pet.getTemperament().toLowerCase()
				.contains(preference.getPreferredTemperament().toLowerCase())) {
			score += weights.getTemperament();
			reasons.add(MatchReason.builder()
				.criterion("temperament")
				.score(weights.getTemperament())
				.message("Matches preferred temperament")
				.build());
		}

		double distanceKm = calculateDistanceKm(
			preference.getPreferredLatitude(),
			preference.getPreferredLongitude(),
			pet.getLatitude(),
			pet.getLongitude()
		);

		if (preference.getSearchRadiusKm() != null &&
			distanceKm >= 0 &&
			distanceKm <= preference.getSearchRadiusKm()) {
			score += weights.getDistance();
			reasons.add(MatchReason.builder()
				.criterion("distance")
				.score(weights.getDistance())
				.message("Within preferred distance")
				.build());
		}

		return PetMatchResponse.builder()
			.petId(pet.getId())
			.petName(pet.getName())
			.breed(pet.getBreed())
			.age(pet.getAge())
			.location(pet.getLocation())
			.adoptionFee(pet.getAdoptionFee())
			.score(score)
			.reasons(reasons)
			.build();
	}

	private double calculateDistanceKm(Double lat1, Double lon1, Double lat2, Double lon2) {
		if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
			return -1;
		}

		final int earthRadiusKm = 6371;

		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);

		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
			+ Math.cos(Math.toRadians(lat1))
			* Math.cos(Math.toRadians(lat2))
			* Math.sin(lonDistance / 2)
			* Math.sin(lonDistance / 2);

		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		return earthRadiusKm * c;
	}
}
