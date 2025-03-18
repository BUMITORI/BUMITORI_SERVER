package org.example.bumitori_server.repository;

import org.example.bumitori_server.entity.Absent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbsentRepository extends JpaRepository<Absent, Long> {
}
