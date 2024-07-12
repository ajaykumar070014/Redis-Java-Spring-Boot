package com.student.Student.service;

import com.redis.service.RedisCacheService;
import com.student.Student.model.Student;
import com.student.Student.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RedisCacheService redisCacheService;

    public List<Student> getAllStudents() {
        List<Student> allStudents = (List<Student>) redisCacheService.get("allStudents");
        if (allStudents == null) {
            allStudents = studentRepository.findAll();
            updateAllStudentsCache(allStudents);
        }
        return allStudents;
    }

    public Student getStudentById(Long id) {
        List<Student> allStudents = (List<Student>) redisCacheService.get("allStudents");
        if (allStudents != null) {
            Optional<Student> studentOptional = allStudents.stream()
                    .filter(student -> student.getId().equals(id))
                    .findFirst();
            if (studentOptional.isPresent()) {
                return studentOptional.get();
            }
        }
        Optional<Student> studentOptional = studentRepository.findById(id);
        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            redisCacheService.set("student:" + id, student);
            if (allStudents != null) {
                allStudents.add(student);
                redisCacheService.set("allStudents", allStudents);
            }
            return student;
        } else {
            throw new RuntimeException("Student not found with id: " + id);
        }
    }


    public Student createStudent(Student student) {
        Student createdStudent = studentRepository.save(student);
        redisCacheService.set(String.valueOf(createdStudent.getId()), createdStudent);
        updateAllStudentsCache();
        return createdStudent;
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student updatedStudent = studentRepository.findById(id)
                .map(student -> {
                    student.setName(studentDetails.getName());
                    student.setEmail(studentDetails.getEmail());
                    student.setDepartment(studentDetails.getDepartment());
                    return studentRepository.save(student);
                })
                .orElseThrow(() -> new RuntimeException("Student not found"));

        redisCacheService.set(String.valueOf(id), updatedStudent);
        updateAllStudentsCache();
        return updatedStudent;
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
        redisCacheService.delete(String.valueOf(id));
        updateAllStudentsCache();
    }

    private void updateAllStudentsCache() {
        List<Student> allStudents = studentRepository.findAll();
        redisCacheService.set("allStudents", allStudents);
    }

    private void updateAllStudentsCache(List<Student> allStudents) {
        redisCacheService.set("allStudents", allStudents);
    }
}