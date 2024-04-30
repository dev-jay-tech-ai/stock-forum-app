package dev.be.serviceuser.repository;

import dev.be.serviceuser.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findByUserName(String userName);
    Optional<UserEntity> findByEmail(String userEmail);
    // List<UserEntity> findByFollowers(UserEntity followers);

}
