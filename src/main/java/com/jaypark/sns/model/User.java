package com.jaypark.sns.model;

import java.sql.Timestamp;

import com.jaypark.sns.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

//TODO: implement
@AllArgsConstructor
@Getter
public class User {

	private Long id;
	private String userName;
	private String password;
	private UserRole userRole;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;


	public static User fromEntity(UserEntity userEntity) {
		return new User(
			userEntity.getId(),
			userEntity.getUserName(),
			userEntity.getPassword(),
			userEntity.getRole(),
			userEntity.getRegisterAt(),
			userEntity.getUpdatedAt(),
			userEntity.getDeletedAt()
		);
	}
}
