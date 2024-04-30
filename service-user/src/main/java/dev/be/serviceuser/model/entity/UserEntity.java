package dev.be.serviceuser.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.be.serviceuser.BaseTimeEntity;
import dev.be.serviceuser.model.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "\"user\"")
@SQLDelete(sql = "UPDATE \"user\" SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@NoArgsConstructor
public class UserEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;
    @Column(nullable = false, name = "user_name", unique = true)
    private String userName;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(nullable = false, name = "email", unique = true)
    private String email;
    private String password;
//    @OneToMany(mappedBy = "following", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private List<FollowEntity> followers;
//    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
//    @JsonIgnore
//    private List<FollowEntity> followings;
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;
    @Column(name = "locked")
    private Boolean locked;
    @Column(name = "enabled")
    private Boolean enabled;

    public static UserEntity of(String userName, String firstName, String lastName, String email, String encodedPwd) {
        UserEntity entity = new UserEntity();
        entity.setUserName(userName);
        entity.setFirstName(firstName);
        entity.setLastName(lastName);
        entity.setEmail(email);
        entity.setPassword(encodedPwd);
        entity.setLocked(false);
        entity.setEnabled(false);
        return entity;
    }
}
