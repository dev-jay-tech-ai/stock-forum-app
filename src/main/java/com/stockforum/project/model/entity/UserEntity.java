package com.stockforum.project.model.entity;

import com.stockforum.project.model.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.sql.Timestamp;
import java.time.Instant;
@Setter
@Getter
@Entity
@Table(name = "\"user\"")
@SQLDelete(sql = "UPDATE \"user\" SET removed_at = NOW() WHERE id=?")
@Where(clause = "deleted_at is NULL")
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;

    @Column(name="user_name", unique = true)
    private String userName;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    @Column(name="registered_at")
    private Timestamp registeredAt;

    @Column(name="updated_at")
    private Timestamp updatedAt;

    @Column(name="deleted_at")
    private Timestamp deletedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }

    public static UserEntity of(String userName, String encodedPwd) {
        UserEntity entity = new UserEntity();
        entity.setUserName(userName);
        entity.setPassword(encodedPwd);
        return entity;
    }
}
