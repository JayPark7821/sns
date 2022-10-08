package com.jaypark.sns.model.event;

import com.jaypark.sns.model.AlarmArgs;
import com.jaypark.sns.model.AlarmType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

//UserEntity userEntity, AlarmType alarmType, AlarmArgs alarmArgs
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AlarmEvent {
	private Long receiveUserId;
	private AlarmType alarmType;
	private AlarmArgs args;
}
