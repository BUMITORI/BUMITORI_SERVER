package org.example.bumitori_server.repository;

import org.example.bumitori_server.entity.Absent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AbsentRepository extends JpaRepository<Absent, Long> {
  List<Absent> findByUserIdInAndAbsentDateBefore(List<Long> userIds, LocalDate date);
  boolean existsByUserIdAndAbsentDate(Long userId, LocalDate absentDate);
  @Query("SELECT COALESCE(MAX(a.id), 0) FROM Absent a")
  Long findMaxId();

}
