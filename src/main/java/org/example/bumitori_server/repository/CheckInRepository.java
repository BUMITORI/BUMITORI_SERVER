package org.example.bumitori_server.repository;

import org.example.bumitori_server.entity.CheckIn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CheckInRepository extends JpaRepository<CheckIn, String> {
}
