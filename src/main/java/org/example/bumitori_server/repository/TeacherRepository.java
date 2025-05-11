package org.example.bumitori_server.repository;

import org.example.bumitori_server.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
  Optional<Teacher> findByGradeAndClassNum(int grade, int classNum);
}