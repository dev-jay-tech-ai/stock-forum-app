package dev.be.serviceuser.model.entity;

import dev.be.serviceuser.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Setter
@Getter
@Entity
@Table(name = "\"profile\"")
@SQLDelete(sql = "UPDATE \"profile\" SET removed_at = NOW() WHERE id=?")
@Where(clause = "removed_at is NULL")
@NoArgsConstructor
public class ProfileEntity extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id = null;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    @Column(name = "description",columnDefinition = "TEXT")
    private String description;

    public static ProfileEntity of(UserEntity user, String profileImgUrl, String description) {
        ProfileEntity entity = new ProfileEntity();
        entity.setProfileImgUrl(profileImgUrl);
        entity.setDescription(description);
        entity.setUser(user);
        return entity;
    }
}

