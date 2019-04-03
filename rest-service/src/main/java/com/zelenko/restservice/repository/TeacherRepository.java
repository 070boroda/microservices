package com.zelenko.restservice.repository;

import com.zelenko.restservice.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository <Teacher,Integer> {
}
