package com.jaypark.sns.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
	DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "User name is duplicated"),
;



	private HttpStatus status;
	private String message;

}
