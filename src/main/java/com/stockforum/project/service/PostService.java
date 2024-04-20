package com.stockforum.project.service;

import com.stockforum.project.exception.ErrorCode;
import com.stockforum.project.exception.ForumApplicationException;
import com.stockforum.project.model.entity.PostEntity;
import com.stockforum.project.model.entity.UserEntity;
import com.stockforum.project.repository.PostEntityRepository;
import com.stockforum.project.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

    @Service
    @RequiredArgsConstructor
    public class PostService {

        private final UserEntityRepository userEntityRepository;
        private final PostEntityRepository postEntityRepository;

        @Transactional
        public void create(String userName, String title, String body) {
            // find user
            UserEntity userEntity = userEntityRepository.findByUserName(userName)
                    .orElseThrow(() -> new ForumApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));
            // save post
            PostEntity postEntity = PostEntity.of(title, body, userEntity);
            postEntityRepository.save(postEntity);
        }

        // entity mapping
        public Page<Post> list(Pageable pageable) {
            return postEntityRepository.findAll(pageable).map(Post::fromEntity);
        }

        public Page<Post> my(Integer userId, Pageable pageable) {
            return postEntityRepository.findAllByUserId(userId, pageable).map(Post::fromEntity);
        }

        @Transactional
        public Post modify(Integer userId, Integer postId, String title, String body) {
            PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new ForumApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
            if (!Objects.equals(postEntity.getUser().getId(), userId)) {
                throw new ForumApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, postId));
            }

            postEntity.setTitle(title);
            postEntity.setBody(body);

            return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
        }

        @Transactional
        public void delete(Integer userId, Integer postId) {
            PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new ForumApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
            if (!Objects.equals(postEntity.getUser().getId(), userId)) {
                throw new ForumApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, postId));
            }
            postEntityRepository.delete(postEntity);
        }


    }