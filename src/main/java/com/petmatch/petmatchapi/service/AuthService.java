package com.petmatch.petmatchapi.service;

import com.petmatch.petmatchapi.dto.auth.AuthRequest;
import com.petmatch.petmatchapi.dto.auth.AuthResponse;
import com.petmatch.petmatchapi.dto.user.UserRegisterRequest;
import com.petmatch.petmatchapi.exception.InvalidCredentialsException;
import com.petmatch.petmatchapi.exception.UserAlreadyExistsException;
import com.petmatch.petmatchapi.model.Role;
import com.petmatch.petmatchapi.model.User;
import com.petmatch.petmatchapi.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(UserRepository userRepository,
		PasswordEncoder passwordEncoder,
		JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public AuthResponse register(UserRegisterRequest request) {
		if (userRepository.findByEmail(request.getEmail()).isPresent()) {
			throw new UserAlreadyExistsException("Account already exists");
		}

		User user = User.builder()
			.name(request.getName())
			.email(request.getEmail())
			.password(passwordEncoder.encode(request.getPassword()))
			.role(Role.USER)
			.build();

		userRepository.save(user);

		String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
		return new AuthResponse(token);
	}

	public AuthResponse login(AuthRequest request) {
		User user = userRepository.findByEmail(request.getEmail())
			.orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid credentials");
		}

		String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
		return new AuthResponse(token);
	}
}
