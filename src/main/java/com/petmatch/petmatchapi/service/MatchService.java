package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.dto.match.PetMatchResponse;
import com.petmatch.petmatchapi.exception.ResourceNotFoundException;
import com.petmatch.petmatchapi.model.Pet;
import com.petmatch.petmatchapi.model.PetStatus;
import com.petmatch.petmatchapi.model.User;
import com.petmatch.petmatchapi.model.UserPreference;
import com.petmatch.petmatchapi.repository.PetRepository;
import com.petmatch.petmatchapi.repository.UserPreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
public class MatchService {

	private static final int DEFAULT_LIMIT = 10;
	private static final int MAX_LIMIT = 50;

	private final UserService userService;
	private final UserPreferenceRepository userPreferenceRepository;
	private final PetRepository petRepository;
	private final PetScoringService petScoringService;

	public MatchService(UserService userService,
		UserPreferenceRepository userPreferenceRepository,
		PetRepository petRepository,
		PetScoringService petScoringService) {
		this.userService = userService;
		this.userPreferenceRepository = userPreferenceRepository;
		this.petRepository = petRepository;
		this.petScoringService = petScoringService;
	}

	@Transactional(readOnly = true)
	public List<PetMatchResponse> getMatchesForCurrentUser(int limit) {
		User currentUser = userService.getCurrentUser();

		UserPreference preference = userPreferenceRepository.findByUser(currentUser)
			.orElseThrow(() -> new ResourceNotFoundException("User preferences not found"));

		List<Pet> candidates = petRepository.searchPets(
			PetStatus.AVAILABLE,
			preference.getPreferredPetType(),
			null,
			preference.getPreferredGender(),
			preference.getPreferredSize(),
			preference.getMinAge(),
			preference.getMaxAge(),
			preference.getPreferredLocation(),
			preference.getMaxAdoptionFee()
		);

		return candidates.stream()
			.map(pet -> petScoringService.calculateMatch(pet, preference))
			.filter(match -> match.getScore() > 0)
			.sorted(Comparator.comparing(PetMatchResponse::getScore).reversed())
			.limit(normalizeLimit(limit))
			.toList();
	}

	private int normalizeLimit(int limit) {
		if (limit <= 0) {
			return DEFAULT_LIMIT;
		}

		return Math.min(limit, MAX_LIMIT);
	}
}
