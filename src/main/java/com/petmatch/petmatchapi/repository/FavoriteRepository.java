package com.petmatch.petmatchapi.repository;

import com.petmatch.petmatchapi.model.Favorite;
import com.petmatch.petmatchapi.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long>
{
	@EntityGraph(attributePaths = "pet")
	List<Favorite> findByUser(User user);

	Optional<Favorite> findByUserIdAndPetId(Long userId, Long petId);

	void deleteAllByPetId(Long petId);

	void deleteAllByUserId(Long userId);
}
