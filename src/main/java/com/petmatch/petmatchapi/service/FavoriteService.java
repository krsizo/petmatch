package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.dto.pet.PetResponse;
import com.petmatch.petmatchapi.exception.ResourceNotFoundException;
import com.petmatch.petmatchapi.model.Favorite;
import com.petmatch.petmatchapi.model.Pet;
import com.petmatch.petmatchapi.model.User;
import com.petmatch.petmatchapi.repository.FavoriteRepository;
import com.petmatch.petmatchapi.repository.PetRepository;
import com.petmatch.petmatchapi.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService
{

	private final FavoriteRepository favoriteRepository;
	private final PetRepository petRepository;
	private final UserService userService;

	public FavoriteService(FavoriteRepository favoriteRepository,
		PetRepository petRepository, UserService userService)
	{
		this.favoriteRepository = favoriteRepository;
		this.petRepository = petRepository;
		this.userService = userService;
	}

	@Transactional
	public Favorite addFavorite(Long petId)
	{
		User currentUser = userService.getCurrentUser();
		Pet pet = petRepository.findById(petId)
			.orElseThrow(() -> new ResourceNotFoundException("Pet not found"));

		favoriteRepository.findByUserIdAndPetId(currentUser.getId(), petId).ifPresent(fav -> {
			throw new ResourceNotFoundException("Pet already in favorites");
		});

		Favorite favorite =
			Favorite.builder().user(currentUser).pet(pet).addedAt(LocalDateTime.now()).build();

		return favoriteRepository.save(favorite);
	}

	@Transactional
	public void removeFavorite(Long petId)
	{
		User currentUser = userService.getCurrentUser();

		Favorite favorite = favoriteRepository.findByUserIdAndPetId(currentUser.getId(), petId)
			.orElseThrow(() -> new ResourceNotFoundException("Not in favorites"));
		favoriteRepository.delete(favorite);
	}

	public List<Favorite> getFavorites()
	{
		User currentUser = userService.getCurrentUser();
		return favoriteRepository.findByUser(currentUser);
	}
}
