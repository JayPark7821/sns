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
import com.jaypark.sns.repository.UserCacheRepository;
import com.jaypark.sns.repository.UserEntityRepository;
import com.jaypark.sns.utils.JwtTokenUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {


	private final UserEntityRepository userEntityRepository;
	private final BCryptPasswordEncoder encoder;
	private final AlarmEntityRepository alarmEntityRepository;
	private final UserCacheRepository userCacheRepository;

	@Value("${jwt.secret-key}")
	private String secretKey;

	@Value("${jwt.token.expired-time-ms}")
	private Long expiredTimeMs;

	public User loadUserByUserName(String username) {
		return userCacheRepository.getUser(username).orElseGet(() ->userEntityRepository.findByUserName(username).map(User::fromEntity)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username))) );
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
		// ?????????????????? ??????
		User user = loadUserByUserName(userName);
			// userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
		userCacheRepository.setUser(user);

		// ???????????? ??????
		if (!encoder.matches(password, user.getPassword())) {
			throw new SnsApplicationException( ErrorCode.INVALID_PASSWORD);
		}
		// ?????? ??????
		return JwtTokenUtils.generateToken(userName, secretKey ,expiredTimeMs );
	}

	// TODO : alarm return
	public Page<Alarm> alarmList(Long userId, Pageable pageable) {
		return alarmEntityRepository.findAllByUserId(userId, pageable).map(Alarm::fromEntity);
	}
}
