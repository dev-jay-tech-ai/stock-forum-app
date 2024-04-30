package dev.be.serviceuser;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Getter
@MappedSuperclass // JPA Entity 클래스들이 BaseTimeEntity을 상속할 경우 필드들 (createdDate, modifiedDate)도 컬럼으로 인식하도록 한다!
@EntityListeners(AuditingEntityListener.class) // BaseTimeEntity 클래스에 Auditing 기능을 포함시킨다!
public abstract class BaseTimeEntity {

    // Entity가 생성되어 저장될 때 시간이 자동 저장된다.
    @CreatedDate
    @Column(updatable = false) // 최초 저장 후 수정 금지
    private Timestamp registeredAt;

    // 조회한 Entity의 값을 변경할 때 시간이 자동 저장된다.
    @LastModifiedDate
    private Timestamp updatedAt;

    @LastModifiedDate
    private Timestamp removedAt;

    @PrePersist
    void registeredAt() {
        this.registeredAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = Timestamp.from(Instant.now());
    }
}
