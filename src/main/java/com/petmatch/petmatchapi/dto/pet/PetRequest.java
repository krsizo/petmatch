package com.petmatch.petmatchapi.dto.pet;

import com.petmatch.petmatchapi.model.Gender;
import com.petmatch.petmatchapi.model.PetType;
import com.petmatch.petmatchapi.model.Size;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PetRequest
{
	@NotBlank
	private String name;
	@Min(0)
	@Max(50)
	@NotNull
	private Integer age;
	@NotBlank
	private String breed;
	@NotNull
	private Gender gender;
	@NotNull
	private Size size;
	@NotNull
	private PetType petType;
	private String temperament;
	private String description;
	private String imageUrl;
	@NotBlank
	private String location;
	@DecimalMin(value = "-90.0")  @DecimalMax("90.0")
	private Double latitude;
	@DecimalMin(value = "-180.0") @DecimalMax("180.0")
	private Double longitude;
	@DecimalMin(value = "0.0", inclusive = false)
	@NotNull
	private BigDecimal adoptionFee;
}
