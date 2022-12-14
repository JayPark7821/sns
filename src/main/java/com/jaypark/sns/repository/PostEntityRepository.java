package com.jaypark.sns.repository;

import com.jaypark.sns.model.entity.LikeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jaypark.sns.model.entity.PostEntity;
import com.jaypark.sns.model.entity.UserEntity;

import java.util.List;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {

	Page<PostEntity> findAllByUser(UserEntity entity, Pageable pageable);

}
