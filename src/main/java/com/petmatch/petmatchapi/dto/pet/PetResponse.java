package com.petmatch.petmatchapi.dto.pet;
import com.petmatch.petmatchapi.model.Gender;
import com.petmatch.petmatchapi.model.PetType;
import com.petmatch.petmatchapi.model.Size;
import com.petmatch.petmatchapi.model.PetStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PetResponse
{
	private Long id;
	private String name;
	private Integer age;
	private String breed;
	private Gender gender;
	private Size size;
	private String temperament;
	private String description;
	private String imageUrl;
	private PetStatus status;
	private PetType petType;
	private String location;
	private Double latitude;
	private Double longitude;
	private BigDecimal adoptionFee;
}
