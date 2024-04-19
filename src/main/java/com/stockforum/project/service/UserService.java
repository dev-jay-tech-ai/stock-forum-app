package com.stockforum.project.service;

import com.stockforum.project.exception.ErrorCode;
import com.stockforum.project.exception.ForumApplicationException;
import com.stockforum.project.model.User;
import com.stockforum.project.model.entity.UserEntity;
import com.stockforum.project.repository.UserEntityRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserEntityRepository userRepository;

    public User join(String userName, String password) {
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new ForumApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s is duplicated.",userName));
        });
        UserEntity userEntity = userRepository.save(UserEntity.of(userName,password)); // register user
        return User.fromEntity(userEntity);
    }

    public String login(String userName, String password) {
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() -> new ForumApplicationException());
        if(!userEntity.getPassword().equals(password)) {
            throw new ForumApplicationException();
        }
        return ""; //  token
    }
}
