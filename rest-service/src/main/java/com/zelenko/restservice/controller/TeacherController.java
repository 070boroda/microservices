package com.zelenko.restservice.controller;


import com.zelenko.Repository.TeacherRepository;
import com.zelenko.entity.Teacher;
import com.zelenko.restservice.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Service
@RestController
@RequestMapping("/")
@RequiredArgsConstructor (onConstructor = @__(@Autowired))
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    @GetMapping("/teachers")
    public ResponseEntity<List<Teacher>> findAllteachers(){
        List<Teacher> teachers = teacherRepository.findAll();
        log.info("Found list teachers");
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

