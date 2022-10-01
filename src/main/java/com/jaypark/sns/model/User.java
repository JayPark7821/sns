package com.jaypark.sns.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.jaypark.sns.model.entity.UserEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;

//TODO: implement
@AllArgsConstructor
@Getter
public class User implements UserDetails {

	private Long id;
	private String userName;
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
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.getUserRole().toString()));
	}

	@Override
	public String getUsername() {
		return this.userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.deletedAt == null;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.deletedAt == null;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.deletedAt == null;
	}

	@Override
	public boolean isEnabled() {
		return this.deletedAt == null;
	}
}
