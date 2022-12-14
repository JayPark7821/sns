package com.jaypark.sns.service;

import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jaypark.sns.exception.ErrorCode;
import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.fixture.PostEntityFixture;
import com.jaypark.sns.fixture.UserEntityFixture;
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
		when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

		Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));

	}

	@Test
	void 포스트작성시_요청한유저가_존재하지않는경우() throws Exception{
		String title = "title";
		String body = "body";
		String userName = "userName";

		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
		when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

		SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,
			() -> postService.create(title, body, userName));
		Assertions.assertEquals(ErrorCode.USER_NOT_FOUND , e.getErrorCode());

	}


	@Test
	void 포스트수정이_성공() throws Exception{
		String title = "title";
		String body = "body";
		String userName = "userName";
		Long postId = 1L;

		PostEntity postEntity = PostEntityFixture.get(userName, postId,1L);
		UserEntity user = postEntity.getUser();

		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(user));
		when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
		when(postEntityRepository.saveAndFlush(any())).thenReturn(postEntity);

		Assertions.assertDoesNotThrow(() -> postService.modify(title, body, userName, postId));

	}


	@Test
	void 포스트수정시_포스트가_존재하지않는_경우() throws Exception{
		String title = "title";
		String body = "body";
		String userName = "userName";
		Long postId = 1L;

		PostEntity postEntity = PostEntityFixture.get(userName, postId, 1L);
		UserEntity user = postEntity.getUser();

		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(user));
		when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

		SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,
			() -> postService.modify(title, body, userName, postId));

		Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());

	}


	@Test
	void 포스트수정시_포스트가_권한이_없는_경우() throws Exception{
		String title = "title";
		String body = "body";
		String userName = "userName";
		Long postId = 1L;

		PostEntity postEntity = PostEntityFixture.get(userName, postId,1L);
		UserEntity writer = UserEntityFixture.get("userName1", "password", 2L);

		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
		when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

		SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,
			() -> postService.modify(title, body, userName, postId));
		Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
	}



	@Test
	void 포스트삭제가_성공() throws Exception{
		String userName = "userName";
		Long postId = 1L;

		PostEntity postEntity = PostEntityFixture.get(userName, postId,1L);
		UserEntity user = postEntity.getUser();

		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(user));
		when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

		Assertions.assertDoesNotThrow(() -> postService.delete(userName, postId));

	}


	@Test
	void 포스트삭제시_포스트가_존재하지않는_경우() throws Exception{
		String userName = "userName";
		Long postId = 1L;

		PostEntity postEntity = PostEntityFixture.get(userName, postId, 1L);
		UserEntity user = postEntity.getUser();

		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(user));
		when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

		SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,
			() -> postService.delete(userName, postId));

		Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, e.getErrorCode());

	}


	@Test
	void 포스트삭제시_포스트가_권한이_없는_경우() throws Exception{
		String userName = "userName";
		Long postId = 1L;

		PostEntity postEntity = PostEntityFixture.get(userName, postId,1L);
		UserEntity writer = UserEntityFixture.get("userName1", "password", 2L);

		when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
		when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

		SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class,
			() -> postService.delete(userName, postId));
		Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, e.getErrorCode());
	}


	@Test
	void 피드목록요청이_성공() throws Exception{

		Pageable pageable = mock(Pageable.class);
		when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());
		Assertions.assertDoesNotThrow(() -> postService.list(pageable));
	}


	@Test
	void 내피드목록요청이_성공() throws Exception{
		Pageable pageable = mock(Pageable.class);
		UserEntity user = mock(UserEntity.class);
		when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(user));
		when(postEntityRepository.findAllByUser(user, pageable)).thenReturn(Page.empty());
		Assertions.assertDoesNotThrow(() -> postService.myList("", pageable));
	}




}
