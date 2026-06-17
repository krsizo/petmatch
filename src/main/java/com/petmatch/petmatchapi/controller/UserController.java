package com.petmatch.petmatchapi.controller;

import com.petmatch.petmatchapi.dto.user.UserRegisterRequest;
import com.petmatch.petmatchapi.dto.user.UserResponse;
import com.petmatch.petmatchapi.dto.user.UserUpdateRequest;
import com.petmatch.petmatchapi.mapper.UserMapper;
import com.petmatch.petmatchapi.model.User;
import com.petmatch.petmatchapi.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/users")

public class UserController
{
	private final UserService userService;
	private final UserMapper userMapper;

	public UserController(UserService userService, UserMapper userMapper) {
		this.userService = userService;
		this.userMapper = userMapper;
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping
	public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRegisterRequest request)
	{
		User created = userService.createUser(request);
		return ResponseEntity.ok(userMapper.toResponse(created));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<UserResponse>> getAllUsers()
	{
		List<UserResponse> users =
			userService.getAllUsers().stream().map(userMapper::toResponse).toList();
		return ResponseEntity.ok(users);
	}

	@GetMapping("/me")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public ResponseEntity<UserResponse> getCurrentUser() {
		User currentUser = userService.getCurrentUser();
		return ResponseEntity.ok(userMapper.toResponse(currentUser));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@GetMapping("/{id}")
	public ResponseEntity<UserResponse> getUser(@PathVariable Long id)
	{
		User existingUser = userService.getUser(id);
		User currentUser = userService.getCurrentUser();

		boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
		boolean isOwner = existingUser.getId().equals(currentUser.getId());

		if (!isAdmin && !isOwner) {
			return ResponseEntity.status(403).build();
		}

		return ResponseEntity.ok(userMapper.toResponse(userService.getUser(id)));
	}

	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	@PutMapping("/{id}")
	public ResponseEntity<UserResponse> updateUser(@PathVariable Long id,
		@Valid @RequestBody UserUpdateRequest request)
	{
		User existingUser = userService.getUser(id);
		User currentUser = userService.getCurrentUser();

		boolean isAdmin = currentUser.getRole().name().equals("ADMIN");
		boolean isOwner = existingUser.getId().equals(currentUser.getId());

		if (!isAdmin && !isOwner) {
			return ResponseEntity.status(403).build();
		}

		User updated = userService.updateUser(id, request);
		return ResponseEntity.ok(userMapper.toResponse(updated));
	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id)
	{
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
}
