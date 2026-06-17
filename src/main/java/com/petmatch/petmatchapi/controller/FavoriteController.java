package com.petmatch.petmatchapi.controller;

import com.petmatch.petmatchapi.dto.favorite.FavoriteResponse;
import com.petmatch.petmatchapi.dto.pet.PetResponse;
import com.petmatch.petmatchapi.mapper.FavoriteMapper;
import com.petmatch.petmatchapi.model.Favorite;
import com.petmatch.petmatchapi.model.Pet;
import com.petmatch.petmatchapi.service.FavoriteService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@PreAuthorize("hasRole('USER')")
public class FavoriteController
{

	private final FavoriteService favoriteService;
	private final FavoriteMapper favoriteMapper;

	public FavoriteController(FavoriteService favoriteService, FavoriteMapper favoriteMapper) {
		this.favoriteService = favoriteService;
		this.favoriteMapper = favoriteMapper;
	}

	@PostMapping("/{petId}")
	public ResponseEntity<FavoriteResponse> addToFavorites(@PathVariable Long petId)
	{
		Favorite favorite = favoriteService.addFavorite(petId);
		return ResponseEntity.ok(favoriteMapper.toResponse(favorite));
	}

	@DeleteMapping("/{petId}")
	public ResponseEntity<Void> removeFromFavorites(@PathVariable Long petId)
	{
		favoriteService.removeFavorite(petId);
		return ResponseEntity.noContent().build();
	}

	@GetMapping
	public ResponseEntity<List<PetResponse>> getUserFavorites()
	{
		List<PetResponse> petResponseList = favoriteService.getFavorites()
			.stream()
			.map(Favorite::getPet)
			.map(favoriteMapper::toPetResponse)
			.toList();
		return ResponseEntity.ok(petResponseList);
	}
}
