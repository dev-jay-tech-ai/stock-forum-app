package dev.be.serviceuser.repository;

import dev.be.serviceuser.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, User> userRedisTemplate;
    private final RedisTemplate<String, String> userTokenTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);

    public void setUser(User user) {
        String key = getKey(user.getUsername());
        log.info("Set User to Redis {}({})", key, user);
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }
    public Optional<User> getUser(String userName) {
        User data = userRedisTemplate.opsForValue().get(getKey(userName));
        log.info("Get User from Redis {}", data);
        return Optional.ofNullable(data);
    }

    public void setToken(User user, String token) {
        String key = getKey(user.getUsername());
        log.info("Set Token to Redis {}({})", key, token);
        userTokenTemplate.opsForValue().set(key, token, USER_CACHE_TTL);
    }
    public Optional<String> getToken(String userName) {
        String data = userTokenTemplate.opsForValue().get(getKey(userName));
        log.info("Get Token from Redis {}", data);
        return Optional.ofNullable(data);
    }

    public void setBlacklist(String userName, String token, Long expiration) {
        String key = getKey(userName);
        log.info("Set Token to Redis {}({})", key, token);
        userTokenTemplate.opsForValue().set(key, token, expiration, TimeUnit.MILLISECONDS);
    }

    public Optional<String> getBlacklist(String userName) {
        String data = userTokenTemplate.opsForValue().get(getKey(userName));
        log.info("Get Token from Redis {}", data);
        return Optional.ofNullable(data);
    }

    public void delete(String userName) {
        // Assuming your key format is "UID:{userName}"
        String key = getKey(userName);
        userTokenTemplate.delete(key);
    }

    private String getKey(String userName) {
        return "UID:" + userName;
    }
}
