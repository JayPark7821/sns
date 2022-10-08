package com.jaypark.sns.repository;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.jaypark.sns.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserCacheRepository {

	private final RedisTemplate<String, User> userRedisTemplate;
	private final static Duration USER_CACHE_TTL = Duration.ofDays(1);

	public void setUser(User user) {
		String key = getKey(user.getUsername());
		log.info("Set User to Redis {} = {} ", key, user);
		userRedisTemplate.opsForValue().set(key, user);
		// userRedisTemplate.opsForValue().setIfAbsent(key, user);
	}

	public Optional<User> getUser(String userName) {
		String key = getKey(userName);
		User user = userRedisTemplate.opsForValue().get(key);
		log.info("Get data from Redis {} = {} ", key, user);
		return Optional.ofNullable(user);
	}

	private String getKey(String userName) {
		return "USER:" + userName;
	}
}
