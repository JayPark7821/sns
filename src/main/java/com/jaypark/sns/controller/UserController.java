package com.jaypark.sns.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaypark.sns.controller.request.UserJoinRequest;
import com.jaypark.sns.controller.request.UserLoginRequest;
import com.jaypark.sns.controller.response.AlarmResponse;
import com.jaypark.sns.controller.response.Response;
import com.jaypark.sns.controller.response.UserJoinResponse;
import com.jaypark.sns.controller.response.UserLoginResponse;
import com.jaypark.sns.exception.ErrorCode;
import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.model.Alarm;
import com.jaypark.sns.model.User;
import com.jaypark.sns.service.UserService;
import com.jaypark.sns.utils.ClassUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	//TODO : implement
	@PostMapping("/join")
	public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
		User user = userService.join(request.getName(), request.getPassword());
		return Response.success(UserJoinResponse.fromUser(user));
	}

	//TODO : implement
	@PostMapping("/login")
	public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
		String token = userService.login(request.getName(), request.getPassword());
		return Response.success(new UserLoginResponse(token));
	}

	@GetMapping("/alarm")
	public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
		User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
			.orElseThrow(() -> new SnsApplicationException(
				ErrorCode.INTERNAL_SERVER_ERROR, "Casting to user failed"));

		return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));
	}

}
