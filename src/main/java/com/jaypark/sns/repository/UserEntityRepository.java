package com.jaypark.sns.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaypark.sns.model.entity.UserEntity;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity,Long> {
	Optional<UserEntity> findByUserName(String userName);
}
