package com.jaypark.sns.model;

import java.sql.Timestamp;

import com.jaypark.sns.model.entity.AlarmEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Alarm {
	private Long id;
	private AlarmType alarmType;
	private AlarmArgs args;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;

	public static Alarm fromEntity(AlarmEntity alarmEntity) {
		return new Alarm(
			alarmEntity.getId(),
			alarmEntity.getAlarmType(),
			alarmEntity.getArgs(),
			alarmEntity.getRegisterAt(),
			alarmEntity.getUpdatedAt(),
			alarmEntity.getDeletedAt()
		);
	}
}
