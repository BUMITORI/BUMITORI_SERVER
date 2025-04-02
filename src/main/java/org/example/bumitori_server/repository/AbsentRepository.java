package org.example.bumitori_server.repository;

import org.example.bumitori_server.entity.Absent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AbsentRepository extends JpaRepository<Absent, Long> {

    boolean existsByUserIdAndAbsentDate(Long userId, LocalDate absentDate);
}
