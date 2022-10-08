package com.jaypark.sns.service;

import com.jaypark.sns.model.AlarmArgs;
import com.jaypark.sns.model.AlarmType;
import com.jaypark.sns.model.Comment;
import com.jaypark.sns.model.entity.AlarmEntity;
import com.jaypark.sns.model.entity.CommentEntity;
import com.jaypark.sns.model.entity.LikeEntity;
import com.jaypark.sns.repository.AlarmEntityRepository;
import com.jaypark.sns.repository.CommentEntityRepository;
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

@Service
@RequiredArgsConstructor
public class PostService {

	private final PostEntityRepository postEntityRepository;
	private final UserEntityRepository userEntityRepository;
	private final LikeEntityRepository likeEntityRepository;
	private final CommentEntityRepository commentEntityRepository;
	private final AlarmEntityRepository alarmEntityRepository;
	private final AlarmService alarmService;


	@Transactional
	public void create(String title, String body, String userName) {
		UserEntity user = getUserOrException(userName);
		postEntityRepository.save(PostEntity.of(title, body, user));
	}

	@Transactional
	public Post modify(String title, String body, String userName, Long postId) {
		UserEntity user = getUserOrException(userName);
		PostEntity postEntity = getPostOrException(postId);
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
		UserEntity user = getUserOrException(userName);
		PostEntity postEntity = getPostOrException(postId);

		if (postEntity.getUser() != user) {
			throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION,
				String.format("%s has no permission with %s", userName, postId));
		}
		likeEntityRepository.deleteAllByPost(postEntity);
		commentEntityRepository.deleteAllByPost(postEntity);
		postEntityRepository.delete(postEntity);

	}

	public Page<Post> list(Pageable pageable) {
		return postEntityRepository.findAll(pageable).map(Post::fromEntity);
	}

	public Page<Post> myList(String userName, Pageable pageable) {
		UserEntity user = getUserOrException(userName);

		return postEntityRepository.findAllByUser(user, pageable).map(Post::fromEntity);
	}

	@Transactional
	public void like(Long postId, String userName) {
		UserEntity user = getUserOrException(userName);
		PostEntity post = getPostOrException(postId);

		// check liked
		likeEntityRepository.findByUserAndPost(user, post).ifPresent(l -> {
			throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("userName %s already like post %d", userName, postId));
		});
		// liked save
		likeEntityRepository.save(LikeEntity.of(user, post));

		AlarmEntity alarmEntity = alarmEntityRepository.save(
			AlarmEntity.of(post.getUser(), AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(user.getId(), postId)));

		alarmService.send(alarmEntity.getId(), post.getUser().getId());
	}

	public long listCount(Long postId) {
		PostEntity post = getPostOrException(postId);
		//count like
		return likeEntityRepository.countByPost(post);
	}

	@Transactional
	public void comment(Long postId, String userName, String comment) {
		UserEntity user = getUserOrException(userName);
		PostEntity post = getPostOrException(postId);

		//comment save
		commentEntityRepository.save(CommentEntity.of(user, post, comment));

		AlarmEntity alarmEntity = alarmEntityRepository.save(
			AlarmEntity.of(post.getUser(), AlarmType.NEW_COMMENT_ON_POST, new AlarmArgs(user.getId(), postId)));
		alarmService.send(alarmEntity.getId(), post.getUser().getId());

	}

	public Page<Comment> getComment(Long postId, Pageable pageable) {
		PostEntity post = getPostOrException(postId);
		return commentEntityRepository.findAllByPost(post, pageable).map(Comment::fromEntity);
	}

	private PostEntity getPostOrException(Long postId) {
		return postEntityRepository.findById(postId)
				.orElseThrow(
						() -> new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));
	}

	private UserEntity getUserOrException(String userName) {
		return userEntityRepository.findByUserName(userName)
				.orElseThrow(
						() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
	}
}
