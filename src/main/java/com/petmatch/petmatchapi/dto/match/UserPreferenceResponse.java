package com.petmatch.petmatchapi.dto.match;

import com.petmatch.petmatchapi.model.Gender;
import com.petmatch.petmatchapi.model.PetType;
import com.petmatch.petmatchapi.model.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class UserPreferenceResponse {
	private Long id;
	private Long userId;
	private PetType preferredPetType;
	private Gender preferredGender;
	private Size preferredSize;
	private Integer minAge;
	private Integer maxAge;
	private BigDecimal maxAdoptionFee;
	private String preferredLocation;
	private Double preferredLatitude;
	private Double preferredLongitude;
	private Double searchRadiusKm;
	private String preferredTemperament;
}
