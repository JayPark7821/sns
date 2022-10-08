package com.jaypark.sns.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.jaypark.sns.model.event.AlarmEvent;
import com.jaypark.sns.service.AlarmService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmConsumer {

	private final AlarmService alarmService;

	@KafkaListener(topics = "${spring.kafka.topic.alarm}")
	public void consumeAlarm(AlarmEvent event, Acknowledgment ack) {
		log.info("Consume the event {}", event);
		alarmService.send(event.getAlarmType(), event.getArgs(), event.getReceiveUserId());
		ack.acknowledge();
	}
}
