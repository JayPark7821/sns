package com.jaypark.sns.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Response<T> {

	private String resultCode;
	private T result;

	public static Response<Void> error(String eerorCode) {
		return new Response<>(eerorCode, null);
	}

	public static <T> Response<T> success(T result) {
		return new Response<>("SUCCESS", result);
	}

}
