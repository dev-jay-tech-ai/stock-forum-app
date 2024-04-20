package com.stockforum.project.service;

import com.stockforum.project.exception.ErrorCode;
import com.stockforum.project.exception.ForumApplicationException;
import com.stockforum.project.model.User;
import com.stockforum.project.model.entity.UserEntity;
import com.stockforum.project.repository.UserEntityRepository;

import com.stockforum.project.service.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        return userRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(() -> new ForumApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));
    }

    public String login(String userName, String password) {
        // if already joined
        UserEntity userEntity = userRepository.findByUserName(userName).orElseThrow(() -> new ForumApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
        // check password
        if(!encoder.matches(password,userEntity.getPassword())) {
            throw new ForumApplicationException(ErrorCode.INVALID_PASSWORD,"%");
        }
        // create and return token
        return JwtTokenUtils.generateAccessToken(userName, secretKey, expiredTimeMs);
    }

    @Transactional
    public User join(String userName, String password) {
        // check username is exceptional
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new ForumApplicationException(ErrorCode.DUPLICATED_USER_NAME,String.format("%s is duplicated.",userName));
        });
        // user save
        UserEntity userEntity = userRepository.save(UserEntity.of(userName, encoder.encode(password))); // register user
        return User.fromEntity(userEntity);
    }

}
