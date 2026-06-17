package com.petmatch.petmatchapi.controller;

import com.petmatch.petmatchapi.dto.match.UserPreferenceRequest;
import com.petmatch.petmatchapi.dto.match.UserPreferenceResponse;
import com.petmatch.petmatchapi.mapper.UserPreferenceMapper;
import com.petmatch.petmatchapi.model.UserPreference;
import com.petmatch.petmatchapi.service.UserPreferenceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/preferences")
@PreAuthorize("hasRole('USER')")
public class UserPreferenceController {

	private final UserPreferenceService userPreferenceService;
	private final UserPreferenceMapper userPreferenceMapper;

	public UserPreferenceController(UserPreferenceService userPreferenceService,
		UserPreferenceMapper userPreferenceMapper) {
		this.userPreferenceService = userPreferenceService;
		this.userPreferenceMapper = userPreferenceMapper;
	}

	@GetMapping("/me")
	public ResponseEntity<UserPreferenceResponse> getMyPreferences() {
		UserPreference preference = userPreferenceService.getCurrentUserPreference();
		return ResponseEntity.ok(userPreferenceMapper.toResponse(preference));
	}

	@PutMapping("/me")
	public ResponseEntity<UserPreferenceResponse> upsertMyPreferences(
		@Valid @RequestBody UserPreferenceRequest request
	) {
		UserPreference preference = userPreferenceService.upsertCurrentUserPreference(request);
		return ResponseEntity.ok(userPreferenceMapper.toResponse(preference));
	}
}
