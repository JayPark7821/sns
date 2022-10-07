package com.jaypark.sns.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.jaypark.sns.model.entity.AlarmEntity;
import com.jaypark.sns.model.entity.UserEntity;

public interface AlarmEntityRepository extends JpaRepository<AlarmEntity, Long> {
	Page<AlarmEntity> findAllByUserId(Long userId, Pageable pageable);
}
