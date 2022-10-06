package com.jaypark.sns.repository;

import com.jaypark.sns.model.entity.LikeEntity;
import com.jaypark.sns.model.entity.PostEntity;
import com.jaypark.sns.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeEntityRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

    @Query("select count(l) " +
            "from LikeEntity l " +
            "where l.post =:post")
    Integer countByPost(@Param("post")PostEntity post);
    List<LikeEntity> findAllByPost(PostEntity post);

}
