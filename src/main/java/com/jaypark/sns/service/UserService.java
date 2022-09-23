package com.jaypark.sns.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jaypark.sns.exception.ErrorCode;
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
		userEntityRepository.findByUserName(userName).ifPresent(user ->{
			throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s is duplicated", userName));
		});

		UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, password));

		return User.fromEntity(userEntity);
	}

	//TODO : implement
	public String login(String userName, String password) {
		// 회원가입여부 체크
		UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,""));
		// 비밀번호 체크
		if (userEntity.getPassword().equals(password)) {
			throw new SnsApplicationException( ErrorCode.DUPLICATED_USER_NAME,"");
		}
		// 토큰 생성
		return "";
	}
}
