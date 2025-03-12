package org.example.bumitori_server.repository;

import org.example.bumitori_server.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
