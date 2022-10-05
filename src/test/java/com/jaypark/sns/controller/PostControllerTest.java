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
import com.jaypark.sns.controller.request.PostCreateRequest;
import com.jaypark.sns.controller.request.PostModifyRequest;
import com.jaypark.sns.controller.request.UserJoinRequest;
import com.jaypark.sns.exception.ErrorCode;
import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.fixture.PostEntityFixture;
import com.jaypark.sns.model.Post;
import com.jaypark.sns.service.PostService;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	ObjectMapper objectMapper;

	@MockBean
	private PostService postService;

	@Test
	@WithMockUser
	void 포스트작성() throws Exception{
		String title = "title";
		String body = "body";

		mvc.perform(post("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
			.andDo(print())
			.andExpect(status().isOk());
	}

	@Test
	@WithAnonymousUser
	void 포스트작성시_로그인하징낳은경우() throws Exception{
		String title = "title";
		String body = "body";

		mvc.perform(post("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, body))))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 포스트수정() throws Exception{
		String title = "title";
		String body = "body";

		when(postService.modify(eq(title), eq(body), any(), any())).thenReturn(
			Post.fromEntity(PostEntityFixture.get("userName", 1L, 1L)));


		mvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
			.andDo(print())
			.andExpect(status().isOk());
	}


	@Test
	@WithAnonymousUser
	void 포스트수정시_로그인하지않은경우() throws Exception{
		String title = "title";
		String body = "body";

		mvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}


	@Test
	@WithMockUser
	void 포스트수정시_본인이_작성한_글이_아니라면_에러발생() throws Exception{
		String title = "title";
		String body = "body";


		doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(body),any(), eq(1L));

		mvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}


	@Test
	@WithMockUser
	void 포스트수정시_수정하려는_글이_없는경우_에러발생() throws Exception{
		String title = "title";
		String body = "body";


		doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(body),any(), eq(1L));

		mvc.perform(put("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, body))))
			.andDo(print())
			.andExpect(status().isNotFound());
	}


	@Test
	@WithMockUser
	void 포스트삭제() throws Exception{
		mvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
	}


	@Test
	@WithAnonymousUser
	void 포스트삭제_로그인하지_않은경우() throws Exception{

		mvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithAnonymousUser
	void 포스트삭제시_작성자와_삭제요청자가_다른경우() throws Exception{
		// mocking
		doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

		mvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}

	@Test
	@WithMockUser
	void 포스트삭제시_삭제하려는_포스트가_존재하지_않을_경우우() throws Exception{
		// mocking
		doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

		mvc.perform(delete("/api/v1/posts/1")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isNotFound());
	}


	@Test
	@WithMockUser
	void 피드목록() throws Exception{
		when(postService.list(any())).thenReturn(Page.empty());

		mvc.perform(get("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
	}


	@Test
	@WithAnonymousUser
	void 피드목록요청시_로그인하지_않은경우() throws Exception{
		when(postService.list(any())).thenReturn(Page.empty());
		mvc.perform(get("/api/v1/posts")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}


	@Test
	@WithMockUser
	void 내피드목록() throws Exception{
		when(postService.myList(any(), any())).thenReturn(Page.empty());
		mvc.perform(get("/api/v1/posts/my")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isOk());
	}


	@Test
	@WithAnonymousUser
	void 내피드목록요청시_로그인하지_않은경우() throws Exception{
		when(postService.myList(any(), any())).thenReturn(Page.empty());
		mvc.perform(get("/api/v1/posts/my")
				.contentType(MediaType.APPLICATION_JSON)
			).andDo(print())
			.andExpect(status().isUnauthorized());
	}


}
