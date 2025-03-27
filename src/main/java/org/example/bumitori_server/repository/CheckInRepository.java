package org.example.bumitori_server.repository;

import org.example.bumitori_server.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    Optional<CheckIn> findByUserId(Long userId);
}
