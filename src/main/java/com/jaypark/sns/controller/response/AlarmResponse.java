package com.jaypark.sns.controller.response;

import java.sql.Timestamp;

import com.jaypark.sns.model.Alarm;
import com.jaypark.sns.model.AlarmArgs;
import com.jaypark.sns.model.AlarmType;
import com.jaypark.sns.model.User;
import com.jaypark.sns.model.entity.AlarmEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AlarmResponse {
	private Long id;
	private AlarmType alarmType;
	private AlarmArgs args;
	private String text;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;

	public static AlarmResponse fromAlarm(Alarm alarm) {
		return new AlarmResponse(
			alarm.getId(),
			alarm.getAlarmType(),
			alarm.getArgs(),
			alarm.getAlarmType().getAlarmText(),
			alarm.getRegisteredAt(),
			alarm.getUpdatedAt(),
			alarm.getDeletedAt()
		);
	}
}
