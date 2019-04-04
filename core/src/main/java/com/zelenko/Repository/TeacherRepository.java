package com.zelenko.Repository;


import com.zelenko.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository <Teacher,Integer> {
}
