package com.jaypark.sns.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import javax.swing.text.html.Option;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.fixture.UserEntityFixture;
import com.jaypark.sns.model.entity.UserEntity;
import com.jaypark.sns.repository.UserEntityRepository;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	UserService userService;

	@Autowired
	private UserEntityRepository userEntityRepository;

	@Test
	void 회원가입이_정상적으로_동작하는_경우() throws Exception{
		String userName = "userName";
		String password = "password";

		// mocking
		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
		when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));

		Assertions.assertDoesNotThrow(() -> userService.join(userName, password));
	}

	@Test
	void 회원가입시_userName으로_회원가입한_유저가_이미_있는경우() throws Exception{
		String userName = "userName";
		String password = "password";

		// mocking
		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));
		when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName, password)));

		Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(userName, password));
	}

	@Test
	void 로그인이_정상적으로_동작() throws Exception{
		String userName = "userName";
		String password = "password";

		UserEntity fixture = UserEntityFixture.get(userName, password);
		// mocking
		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

		Assertions.assertDoesNotThrow(() -> userService.login(userName,password));
	}

	@Test
	void 로그인시_userName으로_회원가입한_유저가_없는_경우() throws Exception{
		String userName = "userName";
		String password = "password";

		// mocking
		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());

		Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(userName, password));
	}

	@Test
	void 로그인시_패스워드가_틀린경우_경우() throws Exception{
		String userName = "userName";
		String password = "password";
		String wrongPassword = "wrongPassword";

		UserEntity fixture = UserEntityFixture.get(userName, password);
		// mocking
		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(fixture));

		Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(userName, wrongPassword));
	}
}
