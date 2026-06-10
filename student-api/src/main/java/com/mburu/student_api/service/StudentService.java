package com.mburu.student_api.service;

import com.mburu.student_api.dto.StudentRequest;
import com.mburu.student_api.dto.StudentResponse;
import com.mburu.student_api.entity.Student;
import com.mburu.student_api.repository.StudentRepository;
import com.mburu.student_api.exception.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class StudentService {
    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private final StudentRepository studentRepository;

    //POST request - creating a new students
    public StudentResponse create(StudentRequest request) {
        log.info("Creating a new student with email: {}", request.getEmail());
        if (studentRepository.existsByEmail(request.getEmail())) {
            log.warn("Error creating student. Email already exists: {}", request.getEmail());
            throw new DuplicateResourceException(
                "Email already in use: " + request.getEmail());
        }
        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .build();
        Student saved = studentRepository.save(student);
        log.info("Student created successfully with ID: {}", saved.getId());
        return toResponse(saved);
    }

    //get request for student/{id}
    public StudentResponse getById(Long id) {
        log.debug("Fetching student with id: {}", id);
        return toResponse(findOrThrow(id));
    }

    //used to handle get Request for all students
    public Page<StudentResponse> getAll(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sortBy));
        return studentRepository.findAll(pageable).map(this::toResponse);
    }

    //paginated get request - searching
    public Page<StudentResponse> search(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));
        return studentRepository.search(keyword, pageable).map(this::toResponse);
    }

    //put request - Updating the endpoint
    public StudentResponse update(Long id, StudentRequest request) {
        log.info("Updating the student with ID: {}", id);
        Student student = findOrThrow(id);
        student.setName(request.getName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());
        log.info("Updated the student with ID: {}", id);
        return toResponse(studentRepository.save(student));
    }
        //delete endpoint
    public void delete(Long id) {
        findOrThrow(id);
        studentRepository.deleteById(id);
    }
    //helprs
    private Student findOrThrow(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Student not found with id: " + id));
    }

    private StudentResponse toResponse(Student student) {
        return StudentResponse.builder()
                .id(student.getId())
                .name(student.getName())
                .email(student.getEmail())
                .age(student.getAge())
                .departmentName(
                    student.getDepartment() != null
                        ? student.getDepartment().getName()
                        : null)
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
