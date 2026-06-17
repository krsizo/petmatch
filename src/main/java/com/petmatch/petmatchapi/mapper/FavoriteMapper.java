package com.petmatch.petmatchapi.mapper;

import com.petmatch.petmatchapi.dto.favorite.FavoriteResponse;
import com.petmatch.petmatchapi.dto.pet.PetResponse;
import com.petmatch.petmatchapi.model.Favorite;
import com.petmatch.petmatchapi.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class FavoriteMapper {

	private final PetMapper petMapper;

	public FavoriteMapper(PetMapper petMapper) {
		this.petMapper = petMapper;
	}

	public FavoriteResponse toResponse(Favorite favorite) {
		if (favorite == null) {
			return null;
		}

		return FavoriteResponse.builder()
			.id(favorite.getId())
			.petId(favorite.getPet().getId())
			.petName(favorite.getPet().getName())
			.petBreed(favorite.getPet().getBreed())
			.petStatus(favorite.getPet().getStatus())
			.build();
	}

	public PetResponse toPetResponse(Favorite favorite) {
		if (favorite == null) {
			return null;
		}

		return petMapper.toResponse(favorite.getPet());
	}

	public PetResponse toPetResponse(Pet pet) {
		return petMapper.toResponse(pet);
	}
}
