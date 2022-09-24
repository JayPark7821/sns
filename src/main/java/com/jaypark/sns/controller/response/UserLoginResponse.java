package com.jaypark.sns.controller.response;

import com.jaypark.sns.model.User;
import com.jaypark.sns.model.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {

	private String token;

}
