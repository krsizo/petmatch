package com.petmatch.petmatchapi.integration;

import com.petmatch.petmatchapi.model.*;
import com.petmatch.petmatchapi.repository.PetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PetRepositoryIntegrationTest extends IntegrationTestBase {

	@Autowired
	private PetRepository petRepository;

	@Test
	void shouldSaveAndFindPet() {
		Pet pet = Pet.builder()
			.name("Buddy")
			.age(3)
			.breed("Labrador Retriever")
			.gender(Gender.MALE)
			.size(Size.MEDIUM)
			.temperament("Friendly")
			.description("Very loyal and playful dog")
			.imageUrl("/images/buddy.jpg")
			.status(PetStatus.AVAILABLE)
			.petType(PetType.DOG)
			.location("Tallinn")
			.adoptionFee(BigDecimal.valueOf(50))
			.build();

		Pet savedPet = petRepository.save(pet);

		assertThat(savedPet.getId()).isNotNull();
		assertThat(petRepository.findById(savedPet.getId())).isPresent();
	}
}
