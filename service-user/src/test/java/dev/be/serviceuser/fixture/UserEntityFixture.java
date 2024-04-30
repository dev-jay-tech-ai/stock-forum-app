package dev.be.serviceuser.fixture;

import com.stockforum.project.model.UserRole;
import dev.be.serviceuser.model.entity.UserEntity;

import java.sql.Timestamp;
import java.time.Instant;

public class UserEntityFixture {

    public static UserEntity get(String userName, String firstName, String lastName, String email, String password) {
        UserEntity entity = new UserEntity();
        entity.setId(1);
        entity.setUserName(userName);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        entity.setPassword(password);
        entity.setRole(UserRole.USER);
        entity.setRegisteredAt(Timestamp.from(Instant.now()));
        return entity;
    }
}
