package com.jaypark.sns.controller.response;

import java.sql.Timestamp;

import com.jaypark.sns.model.Post;
import com.jaypark.sns.model.User;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostResponse {
	private Long id;
	private String title;
	private String body;
	private UserResponse user;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;

	public static PostResponse fromPost(Post post) {
		return new PostResponse(
			post.getId(),
			post.getTitle(),
			post.getBody(),
			UserResponse.fromUser(post.getUser()),
			post.getRegisteredAt(),
			post.getUpdatedAt(),
			post.getDeletedAt()
		);
	}
}

