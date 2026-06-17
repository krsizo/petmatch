package com.petmatch.petmatchapi.dto.match;

import com.petmatch.petmatchapi.model.Gender;
import com.petmatch.petmatchapi.model.PetType;
import com.petmatch.petmatchapi.model.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserPreferenceRequest {

	private PetType preferredPetType;
	private Gender preferredGender;
	private Size preferredSize;

	@Min(0)
	private Integer minAge;

	@Min(0)
	private Integer maxAge;

	@DecimalMin(value = "0.0", inclusive = false)
	private BigDecimal maxAdoptionFee;

	private String preferredLocation;

	private Double preferredLatitude;

	private Double preferredLongitude;

	@DecimalMin(value = "0.1", inclusive = true)
	private Double searchRadiusKm;

	private String preferredTemperament;
}
