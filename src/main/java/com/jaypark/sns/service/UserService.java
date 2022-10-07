package com.jaypark.sns.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaypark.sns.exception.ErrorCode;
import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.model.Alarm;
import com.jaypark.sns.model.User;
import com.jaypark.sns.model.entity.AlarmEntity;
import com.jaypark.sns.model.entity.UserEntity;
import com.jaypark.sns.repository.AlarmEntityRepository;
import com.jaypark.sns.repository.UserEntityRepository;
import com.jaypark.sns.utils.JwtTokenUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {


	private final UserEntityRepository userEntityRepository;
	private final BCryptPasswordEncoder encoder;
	private final AlarmEntityRepository alarmEntityRepository;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.token.expired-time-ms}")
	private Long expiredTimeMs;

	public User loadUserByUserName(String username) {
		return userEntityRepository.findByUserName(username).map(User::fromEntity)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
	}


	//TODO : implement
	@Transactional
	public User join(String userName, String password) {

		userEntityRepository.findByUserName(userName).ifPresent(user -> {
			throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME,
				String.format("%s is duplicated", userName));
		});

		UserEntity userEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));

		return User.fromEntity(userEntity);
	}

	//TODO : implement
	public String login(String userName, String password) {
		// 회원가입여부 체크
		UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

		// 비밀번호 체크
		if (!encoder.matches(password, userEntity.getPassword())) {
			throw new SnsApplicationException( ErrorCode.INVALID_PASSWORD);
		}
		// 토큰 생성
		return JwtTokenUtils.generateToken(userName, secretKey ,expiredTimeMs );
	}

	// TODO : alarm return
	public Page<Alarm> alarmList(Long userId, Pageable pageable) {
		return alarmEntityRepository.findAllByUserId(userId, pageable).map(Alarm::fromEntity);
	}
}
