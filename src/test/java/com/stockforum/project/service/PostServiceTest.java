package com.stockforum.project.service;

import com.stockforum.project.exception.ErrorCode;
import com.stockforum.project.exception.ForumApplicationException;
import com.stockforum.project.fixture.TestInfoFixture;
import com.stockforum.project.fixture.UserEntityFixture;
import com.stockforum.project.model.entity.PostEntity;
import com.stockforum.project.model.entity.UserEntity;
import com.stockforum.project.repository.PostEntityRepository;
import com.stockforum.project.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    PostService postService;

    @MockBean
    UserEntityRepository userEntityRepository;

    @MockBean
    PostEntityRepository postEntityRepository;

    @Test
    void create_post() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(UserEntityFixture.get(fixture.getUserName(), fixture.getPassword())));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        Assertions.assertDoesNotThrow(() -> postService.create(fixture.getUserName(), fixture.getTitle(), fixture.getBody()));
    }

    @Test
    void create_post_without_login() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        ForumApplicationException exception = Assertions.assertThrows(ForumApplicationException.class, () -> postService.create(fixture.getUserName(), fixture.getTitle(), fixture.getBody()));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
    @Test
    void no_post_edit() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());
        ForumApplicationException exception = Assertions.assertThrows(ForumApplicationException.class, () ->
                postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void no_post_user_edit() {

        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        ForumApplicationException exception = Assertions.assertThrows(ForumApplicationException.class, () -> postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void not_sync_user_writer_edit() {
        PostEntity mockPostEntity = mock(PostEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPostEntity));
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getUser()).thenReturn(mock(UserEntity.class));
        ForumApplicationException exception = Assertions.assertThrows(ForumApplicationException.class, () -> postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }

    @Test
    void no_post_delete() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());
        ForumApplicationException exception = Assertions.assertThrows(ForumApplicationException.class, () -> postService.delete(fixture.getUserId(), fixture.getPostId()));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void no_user_delete() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        ForumApplicationException exception = Assertions.assertThrows(ForumApplicationException.class, () -> postService.delete(fixture.getUserId(), fixture.getPostId()));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void not_sync_user_writer_delete () {
        PostEntity mockPostEntity = mock(PostEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);

        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postEntityRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPostEntity));
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getUser()).thenReturn(mock(UserEntity.class));
        ForumApplicationException exception = Assertions.assertThrows(ForumApplicationException.class, () -> postService.delete(fixture.getUserId(), fixture.getPostId()));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }


    @Test
    void no_user_postlist() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(userEntityRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        ForumApplicationException exception = Assertions.assertThrows(ForumApplicationException.class, () -> postService.my(fixture.getUserId(), mock(Pageable.class)));

        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void ok_postlist() {
        Pageable pageable = mock(Pageable.class);
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    void ok_my_postlist() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        Pageable pageable = mock(Pageable.class);
        when(postEntityRepository.findAllByUserId(any(), pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.my(fixture.getUserId(), pageable));
    }


}
