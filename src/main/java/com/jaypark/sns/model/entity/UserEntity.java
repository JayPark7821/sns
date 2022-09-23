package com.jaypark.sns.model.entity;

import java.lang.annotation.Target;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class UserEntity {

	@Id
	private Long id;
	@Column(name = "user_name")
	private String userName;
	@Column(name = "password")
	private String password;

}
