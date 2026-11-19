package com.student.student_management_system.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
import com.student.student_management_system.model.student;
public interface StudentRepository extends JpaRepository<student, String> {
    Optional<student> findByEmail(String email);
    List<student> findByCourseName(String courseName);
}
    