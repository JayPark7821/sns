package com.jaypark.sns.model;

import java.sql.Timestamp;

import com.jaypark.sns.model.entity.PostEntity;

public class Post {
	private Integer id;
	private String title;
	private String body;
	private User user;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;

	public static Post fromEntity(PostEntity entity) {
		// return new Post(
		// 	entity.getId(),
		// 	entity.getTitle(),
		// 	entity.getBody(),
		// 	User.fromEntity(entity.getUser()),
		// 	entity.getRegisterAt(),
		// 	entity.getUpdatedAt(),
		// 	entity.getDeletedAt()
		// );
		return null;
	}

}
