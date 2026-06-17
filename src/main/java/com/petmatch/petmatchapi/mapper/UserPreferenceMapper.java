package com.petmatch.petmatchapi.mapper;

import com.petmatch.petmatchapi.dto.match.UserPreferenceResponse;
import com.petmatch.petmatchapi.model.UserPreference;
import org.springframework.stereotype.Component;

@Component
public class UserPreferenceMapper {

	public UserPreferenceResponse toResponse(UserPreference preference) {
		if (preference == null) {
			return null;
		}

		return UserPreferenceResponse.builder()
			.id(preference.getId())
			.userId(preference.getUser().getId())
			.preferredPetType(preference.getPreferredPetType())
			.preferredGender(preference.getPreferredGender())
			.preferredSize(preference.getPreferredSize())
			.minAge(preference.getMinAge())
			.maxAge(preference.getMaxAge())
			.maxAdoptionFee(preference.getMaxAdoptionFee())
			.preferredLocation(preference.getPreferredLocation())
			.preferredLatitude(preference.getPreferredLatitude())
			.preferredLongitude(preference.getPreferredLongitude())
			.searchRadiusKm(preference.getSearchRadiusKm())
			.preferredTemperament(preference.getPreferredTemperament())
			.build();
	}
}
