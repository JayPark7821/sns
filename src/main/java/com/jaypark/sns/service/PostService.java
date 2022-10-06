package com.jaypark.sns.service;

import com.jaypark.sns.model.entity.LikeEntity;
import com.jaypark.sns.repository.LikeEntityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaypark.sns.exception.ErrorCode;
import com.jaypark.sns.exception.SnsApplicationException;
import com.jaypark.sns.model.Post;
import com.jaypark.sns.model.entity.PostEntity;
import com.jaypark.sns.model.entity.UserEntity;
import com.jaypark.sns.repository.PostEntityRepository;
import com.jaypark.sns.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostEntityRepository postEntityRepository;
	private final UserEntityRepository userEntityRepository;
	private final LikeEntityRepository likeEntityRepository;

	@Transactional
	public void create(String title, String body, String userName) {
		UserEntity user = userEntityRepository.findByUserName(userName)
			.orElseThrow(
				() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
		postEntityRepository.save(PostEntity.of(title, body, user));
	}

	@Transactional
	public Post modify(String title, String body, String userName, Long postId) {
		UserEntity user = userEntityRepository.findByUserName(userName)
			.orElseThrow(
				() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
		// post exist
		PostEntity postEntity = postEntityRepository.findById(postId)
			.orElseThrow(
				() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
		// post permission
		if (postEntity.getUser() != user) {
			throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
				String.format("%s has no permission with %s", userName, postId));
		}
		postEntity.setTitle(title);
		postEntity.setBody(body);

		return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
	}

	@Transactional
	public void delete(String userName, Long postId) {
		UserEntity user = userEntityRepository.findByUserName(userName)
			.orElseThrow(
				() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

		PostEntity postEntity = postEntityRepository.findById(postId)
			.orElseThrow(
				() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

		if (postEntity.getUser() != user) {
			throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
				String.format("%s has no permission with %s", userName, postId));
		}

		postEntityRepository.delete(postEntity);

	}

	public Page<Post> list(Pageable pageable) {
		return postEntityRepository.findAll(pageable).map(Post::fromEntity);
	}

	public Page<Post> myList(String userName, Pageable pageable) {
		UserEntity user = userEntityRepository.findByUserName(userName)
			.orElseThrow(
				() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

		return postEntityRepository.findAllByUser(user, pageable).map(Post::fromEntity);
	}

	@Transactional
	public void like(Long postId, String userName) {
		UserEntity user = userEntityRepository.findByUserName(userName)
				.orElseThrow(
						() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

		PostEntity post = postEntityRepository.findById(postId)
				.orElseThrow(
						() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

		// check liked
		likeEntityRepository.findByUserAndPost(user, post).ifPresent(l -> {
			throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already like post %d", userName, postId));
		});
		// liked save
		likeEntityRepository.save(LikeEntity.of(user, post));

	}

	public int listCount(Long postId) {
		PostEntity post = postEntityRepository.findById(postId)
				.orElseThrow(
						() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
		//count like
		return likeEntityRepository.countByPost(post);
	}
}
