package dev.be.serviceuser.controller;

import dev.be.serviceuser.model.entity.UserEntity;
import dev.be.serviceuser.repository.EmailRepository;
import dev.be.serviceuser.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {
    private final UserEntityRepository userEntityRepository;

    private final EmailRepository redisEmailRepository;
    @GetMapping("/verification")
    public String confirm(@RequestParam("email") String email, @RequestParam("code") String code) {
        Optional<String> optionalRedisAuthCode = redisEmailRepository.getAuthCode(email);
        String redisAuthCode = optionalRedisAuthCode.orElseThrow(() -> new RuntimeException("Redis auth code not found for email: " + email));
        // compare param code
        if (!code.trim().equals(redisAuthCode.trim())) {
            return code + redisAuthCode + "Invalid verification code";
        }
        // If the codes match, update the user's status to enable
        try {
            // Update the user's status to enabled
            UserEntity userEntity = userEntityRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found for email: " + email));
            userEntity.setEnabled(true);
            userEntityRepository.save(userEntity);
            return "Email verified successfully";
        } catch (Exception e) {
            log.error("Error verifying email: {}", e.getMessage());
            return "Error verifying email";
        }
    }
}