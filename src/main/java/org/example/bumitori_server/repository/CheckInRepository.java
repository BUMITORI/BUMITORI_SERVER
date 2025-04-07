package org.example.bumitori_server.repository;

import org.example.bumitori_server.entity.CheckIn;
import org.example.bumitori_server.enums.EnterStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
  Optional<CheckIn> findByUserId(Long userId);
  List<CheckIn> findByUserIdIn(List<Long> userIds);

  @Modifying(clearAutomatically = true)
  @Query("UPDATE CheckIn c SET c.enterStatus = :newStatus " +
      "WHERE c.userId IN :userIds AND c.enterStatus = :currentStatus")
  int bulkUpdateEnterStatus(@Param("userIds") List<Long> userIds,
                            @Param("currentStatus") EnterStatus currentStatus,
                            @Param("newStatus") EnterStatus newStatus);
}

