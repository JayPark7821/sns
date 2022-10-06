package com.jaypark.sns.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaypark.sns.controller.request.UserJoinRequest;
import com.jaypark.sns.controller.request.UserLoginRequest;
import com.jaypark.sns.exception.ErrorCode;
import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.model.User;
import com.jaypark.sns.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	UserService userService;


	@Test
	void 회원가입() throws Exception{

		String userName = "userName";
		String password = "password";

		when(userService.join(userName, password)).thenReturn(mock(User.class));

		mvc.perform(post("/api/v1/users/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password))))
			.andDo(print())
			.andExpect(status().isOk());

	}

	@Test
	void 회원가입시_이미_회원가입된_userName으로_회원가입을_하는경우_에러() throws Exception{

		String userName = "userName";
		String password = "password";

		when(userService.join(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""));

		mvc.perform(post("/api/v1/users/join")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserJoinRequest(userName, password))))
			.andDo(print())
			.andExpect(status().isConflict());

	}


	@Test
	void 로그인() throws Exception{

		String userName = "userName";
		String password = "password";

		when(userService.login(userName, password)).thenReturn("test_token");

		mvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
			.andDo(print())
			.andExpect(status().isOk());

	}

	@Test
	void 로그인시_회원가입이_안된_userName을_입력할경우_에러반환() throws Exception{

		String userName = "userName";
		String password = "password";

		when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

		mvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
			.andDo(print())
			.andExpect(status().isNotFound());

	}

	@Test
	void 로그인시_틀린_password를_입력할경우_에러반환() throws Exception{

		String userName = "userName";
		String password = "password";

		when(userService.login(userName, password)).thenThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD));

		mvc.perform(post("/api/v1/users/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new UserLoginRequest(userName, password))))
			.andDo(print())
			.andExpect(status().isUnauthorized());

	}

	@Test
	@WithMockUser
	void 알람기능() throws Exception {
		when(userService.alarmList(any(), any())).thenReturn(Page.empty());
		mvc.perform(get("/api/v1/users/alaram")
				.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	void 알람리스트요청시_로그인하지_않은경우() throws Exception {
		when(userService.alarmList(any(), any())).thenReturn(Page.empty());
		mvc.perform(get("/api/v1/users/alaram")
				.contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
				.andExpect(status().isUnauthorized());
	}
}
