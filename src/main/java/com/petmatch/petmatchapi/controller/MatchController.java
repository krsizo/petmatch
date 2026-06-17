package com.petmatch.petmatchapi.controller;

import com.petmatch.petmatchapi.dto.match.PetMatchResponse;
import com.petmatch.petmatchapi.service.MatchService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@PreAuthorize("hasRole('USER')")
public class MatchController {

	private final MatchService matchService;

	public MatchController(MatchService matchService) {
		this.matchService = matchService;
	}

	@GetMapping("/me")
	public ResponseEntity<List<PetMatchResponse>> getMyMatches(
		@RequestParam(defaultValue = "10") int limit
	) {
		return ResponseEntity.ok(matchService.getMatchesForCurrentUser(limit));
	}
}
