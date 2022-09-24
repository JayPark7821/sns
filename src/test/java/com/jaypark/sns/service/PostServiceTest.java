package com.jaypark.sns.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.jaypark.sns.exception.ErrorCode;
import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.model.entity.PostEntity;
import com.jaypark.sns.model.entity.UserEntity;
import com.jaypark.sns.repository.PostEntityRepository;
import com.jaypark.sns.repository.UserEntityRepository;

@SpringBootTest
public class PostServiceTest {

	@Autowired
	PostService postService;
	@MockBean
	PostEntityRepository postEntityRepository;
	@MockBean
	UserEntityRepository userEntityRepository;

	@Test
	void 포스트작성_성공() throws Exception{
		String title = "title";
		String body = "body";
		String userName = "userName";

		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
		when(postEntityRepository.save(any())).thenReturn(PostEntity.class);

		Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));

	}

	@Test
	void 포스트작성시_요청한유저가_존재하지않는경우() throws Exception{
		String title = "title";
		String body = "body";
		String userName = "userName";

		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
		when(postEntityRepository.save(any())).thenReturn(PostEntity.class);

		SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,
			() -> postService.create(title, body, userName));
		Assertions.assertEquals(ErrorCode.USER_NOT_FOUND , e.getErrorCode());

	}
}
