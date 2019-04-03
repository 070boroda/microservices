package com.zelenko.restservice.controller;

import com.zelenko.restservice.entity.Teacher;
import com.zelenko.restservice.exception.NotFoundException;
import com.zelenko.restservice.repository.TeacherRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class TeacherController {

    @Autowired
    TeacherRepository teacherRepository;

    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> findAllteachers(){
        List<Teacher> teachers = teacherRepository.findAll();
        if(teachers.isEmpty()){
            return new ResponseEntity<>(teachers, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(teachers,HttpStatus.OK);
    }

    @GetMapping("/teacher/{id}")
    public Teacher findOne(@PathVariable Integer id){
        System.out.println(teacherRepository.getOne(id).toString());
        return teacherRepository.getOne(id);
    }

    @PostMapping("/teacher")
    public ResponseEntity<Teacher> newTeacher(@RequestBody Teacher teacher) {
        teacherRepository.save(teacher);
        return new ResponseEntity<>(teacher, HttpStatus.CREATED);
    }

    @PutMapping("/teacher/{id}")
    public ResponseEntity<?> updateTeacher(@PathVariable Integer id,@RequestBody Teacher teacher){
        Teacher teacherFromDb = teacherRepository.getOne(id);
        if(teacherFromDb == null){
            return new ResponseEntity<>(new NotFoundException(id), HttpStatus.NOT_FOUND);
        }
        teacherFromDb.setFirstName(teacher.getFirstName());
        teacherFromDb.setSecondName(teacher.getSecondName());
        teacherRepository.save(teacherFromDb);
        return new ResponseEntity<>(teacherFromDb, HttpStatus.OK);
    }

    @DeleteMapping("/teacher/{id}")
    public ResponseEntity<?> deleteTeacher(@PathVariable Integer id){
        Teacher teacher = teacherRepository.getOne(id);
        if(teacher == null){
            return new ResponseEntity<>(new NotFoundException(id), HttpStatus.NOT_FOUND);
        }
        teacherRepository.deleteById(id);
        return new ResponseEntity<Teacher>(HttpStatus.NO_CONTENT);
    }
}

