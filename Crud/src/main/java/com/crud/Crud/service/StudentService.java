package com.crud.Crud.service;

import com.crud.Crud.entity.Student;
import com.crud.Crud.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Cacheable(value = "student")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Cacheable(value = "student", key = "#id")
            public Student getStudentById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    @CachePut(value = "student", key = "#result.id")
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    @CachePut(value = "student", key = "#id")
    public Student updateStudent(Long id, Student studentDetails) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setName(studentDetails.getName());
            student.setAge(studentDetails.getAge());
            student.setGender(studentDetails.getGender());
            return studentRepository.save(student);
        }
        return null;
    }

    @Caching(
            evict = {@CacheEvict(value = "student", allEntries = true), @CacheEvict(value="student", key="#id")
            })
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}