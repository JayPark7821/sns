package com.jaypark.sns.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.jaypark.sns.model.event.AlarmEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmProducer {

	private final KafkaTemplate<Long,AlarmEvent> kafkaTemplate;

	@Value("${spring.kafka.topic.alarm}")
	private String topic;

	public void send(AlarmEvent event) {
		kafkaTemplate.send(topic, event.getReceiveUserId(), event);
		log.info("Send to Kafka finished");
	}
}
