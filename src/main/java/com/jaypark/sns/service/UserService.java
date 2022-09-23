package com.jaypark.sns.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.model.User;
import com.jaypark.sns.model.entity.UserEntity;
import com.jaypark.sns.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {


	private final UserEntityRepository userEntityRepository;

	//TODO : implement
	public User join(String userName, String password) {
		Optional<UserEntity> userEntity = userEntityRepository.findByUserName(userName);
		userEntityRepository.save(new UserEntity());
		return new User();
	}

	//TODO : implement
	public String login(String userName, String password) {
		// 회원가입여부 체크
		UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(SnsApplicationException::new);
		// 비밀번호 체크
		if (userEntity.getPassword().equals(password)) {
			throw new SnsApplicationException();
		}
		// 토큰 생성
		return "";
	}
}
