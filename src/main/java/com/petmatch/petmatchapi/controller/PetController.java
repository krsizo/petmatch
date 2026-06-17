package com.petmatch.petmatchapi.controller;

import com.petmatch.petmatchapi.dto.pet.PetRequest;
import com.petmatch.petmatchapi.dto.pet.PetResponse;
import com.petmatch.petmatchapi.mapper.PetMapper;
import com.petmatch.petmatchapi.model.Gender;
import com.petmatch.petmatchapi.model.Pet;
import com.petmatch.petmatchapi.model.PetStatus;
import com.petmatch.petmatchapi.model.PetType;
import com.petmatch.petmatchapi.model.Size;
import com.petmatch.petmatchapi.service.PetService;
import com.petmatch.petmatchapi.dto.common.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController
{

	private final PetService petService;
	private final PetMapper petMapper;

	public PetController(PetService petService, PetMapper petMapper)
	{
		this.petService = petService;
		this.petMapper = petMapper;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<PetResponse> createPet(@Valid @RequestBody PetRequest dto)
	{
		Pet created = petService.createPet(dto);
		return ResponseEntity.ok(petMapper.toResponse(created));
	}

	@GetMapping
	public ResponseEntity<PageResponse<PetResponse>> getAllPets(
		@PageableDefault(size = 20, sort = "id", direction = Sort.Direction.ASC)
		Pageable pageable)
	{
		Page<Pet> petPage = petService.getAllPets(pageable);

		List<PetResponse> pets = petPage.getContent().stream().map(petMapper::toResponse).toList();

		PageResponse<PetResponse> response = PageResponse.<PetResponse>builder()
			.content(pets)
			.page(petPage.getNumber())
			.size(petPage.getSize())
			.totalElements(petPage.getTotalElements())
			.totalPages(petPage.getTotalPages())
			.last(petPage.isLast())
			.build();

		return ResponseEntity.ok(response);
	}

	@GetMapping("/type/{type}")
	public ResponseEntity<List<PetResponse>> getPetsByType(@PathVariable PetType type)
	{
		List<PetResponse> pets =
			petService.getPetsByType(type).stream().map(petMapper::toResponse).toList();
		return ResponseEntity.ok(pets);
	}

	@GetMapping("/{id}")
	public ResponseEntity<PetResponse> getPet(@PathVariable Long id)
	{
		return ResponseEntity.ok(petMapper.toResponse(petService.getPet(id)));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/{id}")
	public ResponseEntity<PetResponse> updatePet(@PathVariable Long id,
		@Valid @RequestBody PetRequest dto)
	{
		Pet updated = petService.updatePet(id, dto);
		return ResponseEntity.ok(petMapper.toResponse(updated));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePet(@PathVariable Long id)
	{
		petService.deletePet(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/search")
	public ResponseEntity<List<PetResponse>> searchPets(
		@RequestParam(required = false) PetStatus status,
		@RequestParam(required = false) PetType petType,
		@RequestParam(required = false) String breed, @RequestParam(required = false) Gender gender,
		@RequestParam(required = false) Size size, @RequestParam(required = false) Integer minAge,
		@RequestParam(required = false) Integer maxAge,
		@RequestParam(required = false) String location,
		@RequestParam(required = false) BigDecimal maxFee)
	{
		List<PetResponse> pets =
			petService.searchPets(status, petType, breed, gender, size, minAge, maxAge, location,
				maxFee).stream().map(petMapper::toResponse).toList();
		return ResponseEntity.ok(pets);
	}

	@GetMapping("/nearby")
	public ResponseEntity<List<PetResponse>> getNearbyPets(@RequestParam Double lat,
		@RequestParam Double lng, @RequestParam(defaultValue = "20") Double radiusKm)
	{
		List<Pet> pets = petService.findNearbyPets(lat, lng, radiusKm);
		return ResponseEntity.ok(pets.stream().map(petMapper::toResponse).toList());
	}
}
