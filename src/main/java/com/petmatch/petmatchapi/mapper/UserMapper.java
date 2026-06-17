package com.petmatch.petmatchapi.mapper;

import com.petmatch.petmatchapi.dto.user.UserResponse;
import com.petmatch.petmatchapi.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

	public UserResponse toResponse(User user) {
		if (user == null) {
			return null;
		}

		return UserResponse.builder()
			.id(user.getId())
			.name(user.getName())
			.email(user.getEmail())
			.role(user.getRole())
			.build();
	}
}
