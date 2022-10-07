package com.jaypark.sns.repository;

import com.jaypark.sns.model.entity.CommentEntity;
import com.jaypark.sns.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentEntityRepository extends JpaRepository<CommentEntity, Long> {

    Page<CommentEntity> findAllByPost(PostEntity post, Pageable pageable);


	@Modifying
	@Query("update CommentEntity c set c.deletedAt = now() where c.post =:postEntity")
	void deleteAllByPost(PostEntity postEntity);

}
