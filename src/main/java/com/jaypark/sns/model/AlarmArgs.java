package com.jaypark.sns.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class AlarmArgs {

	private Long fromUserId;
	private Long targetId;

}
