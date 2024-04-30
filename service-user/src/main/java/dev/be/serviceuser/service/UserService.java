package dev.be.serviceuser.service;

import dev.be.serviceuser.exception.ErrorCode;
import dev.be.serviceuser.exception.SimpleSnsApplicationException;
import dev.be.serviceuser.model.Profile;
import dev.be.serviceuser.model.User;
import dev.be.serviceuser.model.entity.ProfileEntity;
import dev.be.serviceuser.model.entity.UserEntity;
import dev.be.serviceuser.utils.JwtTokenUtils;
import dev.be.serviceuser.repository.EmailRepository;
import dev.be.serviceuser.repository.ProfileEntityRepository;
import dev.be.serviceuser.repository.UserCacheRepository;
import dev.be.serviceuser.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final UserCacheRepository redisUserRepository;
    private final EmailRepository redisEmailRepository;
    private final ProfileEntityRepository profileRepository;

    private final MailService mailService;

    // Inside your class
    private static final Logger log = LoggerFactory.getLogger(User.class);

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        return redisUserRepository.getUser(userName).orElseGet(
                () -> userRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(
                        () -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName))
                ));
    }

    public String login(String userName, String password) {
        User savedUser = loadUserByUsername(userName); // 레디스 캐시에서 찾아보고 없으면 DB에서 검색
        // 레디스에 왜 유저를 저장함 ;;
        // redisUserRepository.setUser(savedUser);
        if (!savedUser.getEnabled()) {
            throw new SimpleSnsApplicationException(ErrorCode.UNVERIFIED_EMAIL, "Your email is not verified");
        }
        if (!encoder.matches(password,savedUser.getPassword())) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        String accessToken = JwtTokenUtils.generateAccessToken(userName, secretKey, expiredTimeMs);
        // redisUserRepository.setToken(savedUser,accessToken);
        // 토큰 저장 할 것
        return accessToken;
    }

    @Transactional
    public User join(String userName, String firstName, String lastName, String email, String password) {
//        boolean isValidEmail = emailValidator.test(email);
//        if (!isValidEmail) {
//            throw new SimpleSnsApplicationException(ErrorCode.INVALID_EMAIL);
//        }
        userRepository.findByEmail(email).ifPresent(it -> {
            throw new SimpleSnsApplicationException(ErrorCode.DUPLICATED_EMAIL, String.format("email is %s", email));
        });
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SimpleSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("userName is %s", userName));
        });
        UserEntity savedUser = userRepository.save(UserEntity.of(userName, firstName, lastName, email, encoder.encode(password)));

        sendCodeToEmail(email);

        return User.fromEntity(savedUser);
    }

    public Profile create(Integer userId, String profileImgUrl, String description) {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userId is %s", userId)));
        ProfileEntity profileEntity = ProfileEntity.of(userEntity, profileImgUrl, description);
        ProfileEntity savedProfile = profileRepository.save(profileEntity);
        return Profile.fromEntity(savedProfile);
    }

    @Transactional
    public Profile modify(Integer userId, String profileImgUrl, String description) {
        ProfileEntity profileEntity = profileRepository.findByUserId(userId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.PROFILE_NOT_FOUND, String.format("Profile not found %s", userId)));
        if (!Objects.equals(profileEntity.getUser().getId(), userId)) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with profile edit %d", userId, "profile"));
        }
        profileEntity.setProfileImgUrl(profileImgUrl);
        profileEntity.setDescription(description);
        return Profile.fromEntity(profileRepository.saveAndFlush(profileEntity));
    }

    @Transactional
    public void logout(User user) {
        String username = user.getUsername();
//        // Get the access token from Redis
//        Optional<String> optionalAccessToken = redisUserRepository.getToken(username);
//        if (optionalAccessToken.isEmpty()) {
//            log.error("Access token not found for user: {}", username);
//            return;
//        }

//        String accessToken = optionalAccessToken.get();
        String accessToken = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InRlc3QiLCJpYXQiOjE3MTQzNjkxNDQsImV4cCI6MTcxNjk2MTE0NH0.MXPz4VPO9dUFHvHFNqfKd4sOvi_y9TTBIR7DW3i7Ops";

        // Calculate the remaining expiration time of the access token
        Long expiration = JwtTokenUtils.getExpiration(accessToken);
        // Store the access token in Redis with a "logout" value and the remaining expiration time
        log.info(username);
        if (expiration > 0) {
            redisUserRepository.setBlacklist(username, accessToken, expiration);
            log.info("Access token invalidated and stored in Redis for user: {}", username);
        } else {
            log.warn("Access token already expired for user: {}", username);
        }

        // Delete the refresh token from Redis
        // redisUserRepository.delete(username);
        // log.info("Refresh token deleted from Redis for user: {}", username);
    }

    public void sendCodeToEmail(String email) {
        String title = "Stock forum mail verification code";
        String authCode = String.valueOf((int)(Math.random() * (90000)) + 100000);
        mailService.sendEmail(email, title, authCode);
        // Store the authentication code in Redis
        redisEmailRepository.setAuthCode(email, authCode);

    }

    public void updatePassword(User user, Integer userId, String newPassword) {
        String username = user.getUsername();
        if (!Objects.equals(user.getId(), userId)) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PERMISSION, "You are not authorized to update this user's password");
        }

        UserEntity userEntity = userRepository.findByUserName(username).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("User with username %s not found", username)));
        String encodedPassword = encoder.encode(newPassword.trim());
        userEntity.setPassword(encodedPassword);
        userRepository.save(userEntity);

    }

}