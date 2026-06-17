package com.petmatch.petmatchapi.dto.favorite;

import com.petmatch.petmatchapi.model.PetStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FavoriteResponse {
	private Long id;
	private Long petId;
	private String petName;
	private String petBreed;
	private PetStatus petStatus;
}
