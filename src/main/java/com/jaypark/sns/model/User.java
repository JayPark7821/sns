package com.jaypark.sns.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jaypark.sns.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

//TODO: implement
@AllArgsConstructor
@Getter
@ToString
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements UserDetails {

	private Long id;
	private String username;
	private String password;
	private UserRole userRole;
	private Timestamp registeredAt;
	private Timestamp updatedAt;
	private Timestamp deletedAt;


	public static User fromEntity(UserEntity userEntity) {
		return new User(
			userEntity.getId(),
			userEntity.getUserName(),
			userEntity.getPassword(),
			userEntity.getRole(),
			userEntity.getRegisterAt(),
			userEntity.getUpdatedAt(),
			userEntity.getDeletedAt()
		);
	}

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
		return this.deletedAt == null;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
		return this.deletedAt == null;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
		return this.deletedAt == null;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
		return this.deletedAt == null;
	}
}
