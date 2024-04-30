package dev.be.serviceuser.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
public class EmailRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private final static Duration AUTH_CACHE_TTL = Duration.ofMinutes(15);

    public EmailRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setAuthCode(String email, String authCode) {
        String key = getKey(email);
        redisTemplate.opsForValue().set(key, authCode, AUTH_CACHE_TTL);
    }

    public Optional<String> getAuthCode(String email) {
        String data = redisTemplate.opsForValue().get(getKey(email));
        return Optional.ofNullable(data);
    }

    private String getKey(String email) {
        return "AuthCode:" + email;
    }

}
