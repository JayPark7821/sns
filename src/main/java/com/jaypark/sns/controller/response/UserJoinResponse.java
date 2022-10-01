package com.jaypark.sns.controller.response;

import com.jaypark.sns.model.User;
import com.jaypark.sns.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserJoinResponse {

	private Long id;
	private String userName;
	private UserRole role;

	public static UserJoinResponse fromUser(User user) {
		return new UserJoinResponse(
			user.getId(),
			user.getUsername(),
			user.getUserRole()
		);
	}
}
