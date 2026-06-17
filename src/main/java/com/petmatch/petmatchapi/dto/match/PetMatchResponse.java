package com.petmatch.petmatchapi.dto.match;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
public class PetMatchResponse {
	private Long petId;
	private String petName;
	private String breed;
	private Integer age;
	private String location;
	private BigDecimal adoptionFee;
	private Integer score;
	private List<MatchReason> reasons;
}
