package com.jaypark.sns.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.jaypark.sns.exception.ErrorCode;
import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.repository.EmitterRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

	private final static Long DEFAULT_TIME_OUT = 60L * 1000 * 60;
	private final static String ALARM_NAME = "alarm";
	private final EmitterRepository emitterRepository;

	public void send(Long alarmId, Long userId) {
		emitterRepository.get(userId).ifPresentOrElse(sseEmitter ->{
			try {
				sseEmitter.send(SseEmitter.event().id(alarmId.toString()).name("ALARM_NAME").data("new Alarm"));
			} catch (IOException e) {
				emitterRepository.delete(userId);
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
