package org.example.bumitori_server.repository;

import org.example.bumitori_server.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String username);
    Optional<UserEntity> findByRfid(String rfid);
}


