package com.jaypark.sns.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaypark.sns.exception.ErrorCode;
import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.model.entity.PostEntity;
import com.jaypark.sns.model.entity.UserEntity;
import com.jaypark.sns.repository.PostEntityRepository;
import com.jaypark.sns.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostEntityRepository postEntityRepository;
	private final UserEntityRepository userEntityRepository;

	@Transactional
	public void create(String title, String body, String userName) {
		UserEntity user = userEntityRepository.findByUserName(userName)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
		postEntityRepository.save(PostEntity.of(title, body, user));
	}

	@Transactional
	public void modify(String title, String body, String userName, Long postId) {
		UserEntity user = userEntityRepository.findByUserName(userName)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
		// post exist

		// post permission
	}
}
