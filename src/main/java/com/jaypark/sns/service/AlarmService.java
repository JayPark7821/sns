package com.jaypark.sns.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.jaypark.sns.exception.ErrorCode;
import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.model.AlarmArgs;
import com.jaypark.sns.model.AlarmType;
import com.jaypark.sns.model.entity.AlarmEntity;
import com.jaypark.sns.model.entity.UserEntity;
import com.jaypark.sns.repository.AlarmEntityRepository;
import com.jaypark.sns.repository.EmitterRepository;
import com.jaypark.sns.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

	private final static Long DEFAULT_TIME_OUT = 60L * 1000 * 60;
	private final static String ALARM_NAME = "alarm";
	private final EmitterRepository emitterRepository;
	private final AlarmEntityRepository alarmEntityRepository;
	private final UserEntityRepository userEntityRepository;

	public void send(AlarmType type, AlarmArgs args, Long receiverUserId) {
		UserEntity user = userEntityRepository.findById(receiverUserId)
			.orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

		AlarmEntity alarmEntity = alarmEntityRepository.save(
			AlarmEntity.of(user, type, args));

		emitterRepository.get(receiverUserId).ifPresentOrElse(sseEmitter ->{
			try {
				sseEmitter.send(SseEmitter.event().id(alarmEntity.getId().toString()).name("ALARM_NAME").data("new Alarm"));
			} catch (IOException e) {
				emitterRepository.delete(receiverUserId);
				throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
			}
		}, () -> log.info("No emitter founded"));
	}

	public SseEmitter connectAlarm(Long userId) {
		SseEmitter sseEmitter = new SseEmitter();
		emitterRepository.save(userId, sseEmitter);
		sseEmitter.onCompletion(() ->emitterRepository.delete(userId));
		sseEmitter.onTimeout(() ->emitterRepository.delete(userId));

		try {
			sseEmitter.send(SseEmitter.event().id("id").name(ALARM_NAME).data("connect completed"));
		} catch (IOException e) {
			throw new SnsApplicationException(ErrorCode.ALARM_CONNECT_ERROR);
		}

		return sseEmitter;
	}
}
