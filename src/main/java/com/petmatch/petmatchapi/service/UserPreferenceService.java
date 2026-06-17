package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.dto.match.UserPreferenceRequest;
import com.petmatch.petmatchapi.exception.ResourceNotFoundException;
import com.petmatch.petmatchapi.model.User;
import com.petmatch.petmatchapi.model.UserPreference;
import com.petmatch.petmatchapi.repository.UserPreferenceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserPreferenceService {

	private final UserPreferenceRepository userPreferenceRepository;
	private final UserService userService;

	public UserPreferenceService(UserPreferenceRepository userPreferenceRepository,
		UserService userService) {
		this.userPreferenceRepository = userPreferenceRepository;
		this.userService = userService;
	}

	@Transactional(readOnly = true)
	public UserPreference getCurrentUserPreference() {
		User currentUser = userService.getCurrentUser();

		return userPreferenceRepository.findByUser(currentUser)
			.orElseThrow(() -> new ResourceNotFoundException("User preferences not found"));
	}

	@Transactional
	public UserPreference upsertCurrentUserPreference(UserPreferenceRequest request) {
		User currentUser = userService.getCurrentUser();

		UserPreference preference = userPreferenceRepository.findByUser(currentUser)
			.orElseGet(() -> UserPreference.builder()
				.user(currentUser)
				.build());

		preference.setPreferredPetType(request.getPreferredPetType());
		preference.setPreferredGender(request.getPreferredGender());
		preference.setPreferredSize(request.getPreferredSize());
		preference.setMinAge(request.getMinAge());
		preference.setMaxAge(request.getMaxAge());
		preference.setMaxAdoptionFee(request.getMaxAdoptionFee());
		preference.setPreferredLocation(request.getPreferredLocation());
		preference.setPreferredLatitude(request.getPreferredLatitude());
		preference.setPreferredLongitude(request.getPreferredLongitude());
		preference.setSearchRadiusKm(request.getSearchRadiusKm());
		preference.setPreferredTemperament(request.getPreferredTemperament());

		return userPreferenceRepository.save(preference);
	}
}
