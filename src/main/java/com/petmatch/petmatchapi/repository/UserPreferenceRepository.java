package com.petmatch.petmatchapi.repository;

import com.petmatch.petmatchapi.model.User;
import com.petmatch.petmatchapi.model.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {

	Optional<UserPreference> findByUser(User user);

	Optional<UserPreference> findByUserId(Long userId);

	boolean existsByUserId(Long userId);
}
