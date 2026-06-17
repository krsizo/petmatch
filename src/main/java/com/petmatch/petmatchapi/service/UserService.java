package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.dto.user.UserRegisterRequest;
import com.petmatch.petmatchapi.dto.user.UserUpdateRequest;
import com.petmatch.petmatchapi.exception.ResourceNotFoundException;
import com.petmatch.petmatchapi.model.Role;
import com.petmatch.petmatchapi.model.User;
import com.petmatch.petmatchapi.repository.BookingRepository;
import com.petmatch.petmatchapi.repository.FavoriteRepository;
import com.petmatch.petmatchapi.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final BookingRepository bookingRepository;
	private final FavoriteRepository favoriteRepository;

	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
		BookingRepository bookingRepository, FavoriteRepository favoriteRepository) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.bookingRepository = bookingRepository;
		this.favoriteRepository = favoriteRepository;
	}

	public User createUser(UserRegisterRequest request) {
		User user = User.builder()
			.name(request.getName())
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(Role.USER)
			.build();
		return userRepository.save(user);
	}

	@Transactional
	public User updateUser(Long id, UserUpdateRequest request) {
		User user = getUser(id);
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		if (request.getPassword() != null && !request.getPassword().isBlank()) {
			user.setPassword(passwordEncoder.encode(request.getPassword()));
		}
		return userRepository.save(user);
	}

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public User getUser(Long id) {
		return userRepository.findById(id)
			.orElseThrow(() -> new ResourceNotFoundException("User not found"));
	}

	@Transactional
	public void deleteUser(Long id) {
		bookingRepository.deleteAllByUserId(id);
		favoriteRepository.deleteAllByUserId(id);

		userRepository.deleteById(id);
	}

	@Transactional(readOnly = true)
	public User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || authentication.getName() == null) {
			throw new ResourceNotFoundException("Current user not found");
		}

		return userRepository.findByEmail(authentication.getName())
			.orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
	}

	public String getCurrentUserEmail()
	{
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return (authentication != null) ? authentication.getName() : null;
	}
}
