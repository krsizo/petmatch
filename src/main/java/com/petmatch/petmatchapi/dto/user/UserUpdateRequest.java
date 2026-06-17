package com.petmatch.petmatchapi.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateRequest
{
	@NotBlank
	private String name;

	@Email
	@NotBlank
	private String email;

	private String password;
}
