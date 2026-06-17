package com.petmatch.petmatchapi.mapper;

import com.petmatch.petmatchapi.dto.pet.PetResponse;
import com.petmatch.petmatchapi.model.Pet;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {

	public PetResponse toResponse(Pet pet) {
		if (pet == null) {
			return null;
		}

		return PetResponse.builder()
			.id(pet.getId())
			.name(pet.getName())
			.age(pet.getAge())
			.breed(pet.getBreed())
			.gender(pet.getGender())
			.size(pet.getSize())
			.temperament(pet.getTemperament())
			.description(pet.getDescription())
			.imageUrl(pet.getImageUrl())
			.status(pet.getStatus())
			.petType(pet.getPetType())
			.location(pet.getLocation())
			.latitude(pet.getLatitude())
			.longitude(pet.getLongitude())
			.adoptionFee(pet.getAdoptionFee())
			.build();
	}
}
