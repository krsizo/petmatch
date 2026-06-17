package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.dto.pet.PetRequest;
import com.petmatch.petmatchapi.exception.ResourceNotFoundException;
import com.petmatch.petmatchapi.model.Gender;
import com.petmatch.petmatchapi.model.Pet;
import com.petmatch.petmatchapi.model.PetType;
import com.petmatch.petmatchapi.model.Size;
import com.petmatch.petmatchapi.model.PetStatus;
import com.petmatch.petmatchapi.repository.BookingRepository;
import com.petmatch.petmatchapi.repository.FavoriteRepository;
import com.petmatch.petmatchapi.repository.PetRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PetService {

	private final PetRepository petRepository;
	private final BookingRepository bookingRepository;
	private final FavoriteRepository favoriteRepository;

	public PetService(PetRepository petRepository, BookingRepository bookingRepository,
		FavoriteRepository favoriteRepository) {
		this.petRepository = petRepository;
		this.bookingRepository = bookingRepository;
		this.favoriteRepository = favoriteRepository;
	}

	public Pet createPet(PetRequest petRequest)
	{
		Pet pet = Pet.builder()
			.name(petRequest.getName())
			.age(petRequest.getAge())
			.breed(petRequest.getBreed())
			.petType(petRequest.getPetType())
			.gender(petRequest.getGender())
			.size(petRequest.getSize())
			.temperament(petRequest.getTemperament())
			.description(petRequest.getDescription())
			.imageUrl(petRequest.getImageUrl())
			.location(petRequest.getLocation())
			.latitude(petRequest.getLatitude())
			.longitude(petRequest.getLongitude())
			.adoptionFee(petRequest.getAdoptionFee())
			.status(PetStatus.AVAILABLE)
			.build();
		return petRepository.save(pet);
	}

	@Transactional
	public Pet updatePet(Long id, PetRequest petRequest) {
		Pet pet = petRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
		pet.setName(petRequest.getName());
		pet.setAge(petRequest.getAge());
		pet.setBreed(petRequest.getBreed());
		pet.setPetType(petRequest.getPetType());
		pet.setGender(petRequest.getGender());
		pet.setSize(petRequest.getSize());
		pet.setTemperament(petRequest.getTemperament());
		pet.setDescription(petRequest.getDescription());
		pet.setImageUrl(petRequest.getImageUrl());
		pet.setLocation(petRequest.getLocation());
		pet.setLatitude(petRequest.getLatitude());
		pet.setLongitude(petRequest.getLongitude());
		pet.setAdoptionFee(petRequest.getAdoptionFee());
		petRepository.save(pet);
		return pet;
	}

	@Transactional(readOnly = true)
	public Page<Pet> getAllPets(Pageable pageable) {
		return petRepository.findAll(pageable);
	}

	public Pet getPet(Long id) {
		Pet pet = petRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
		return pet;
	}

	@Transactional
	public void deletePet(Long id) {
		bookingRepository.deleteAllByPetId(id);
		favoriteRepository.deleteAllByPetId(id);
		petRepository.deleteById(id);
	}

	public List<Pet> getPetsByType(PetType type) {
		List<Pet> pets = petRepository.findByPetType(type);
		return pets;
	}
	
	public List<Pet> searchPets(
		PetStatus status,
		PetType petType,
		String breed,
		Gender gender,
		Size size,
		Integer minAge,
		Integer maxAge,
		String location,
		BigDecimal maxFee
	) {
		return petRepository.searchPets(status, petType, breed, gender, size, minAge, maxAge, location, maxFee);
	}

	public List<Pet> findNearbyPets(Double userLat, Double userLng, Double radiusKm) {
		return petRepository.findNearbyPets(userLat, userLng, radiusKm);
	}
}
