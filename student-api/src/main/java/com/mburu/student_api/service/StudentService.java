package com.mburu.student_api.service;

import com.mburu.student_api.dto.*;
import com.mburu.student_api.entity.*;
import com.mburu.student_api.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor  // --->lombok constructor injection for all final fields
public class StudentService {
    private final StudentRepository studentRepository;
    //  --create a student

    public StudentResponse create(StudentRequest){
        if(studentRepository.existsByEmail(request.getEmail())){
            throw new IllegatlArgumentException("Email already exists" + request.getEmail());
        }
        Student student = Student.builder()
                .name(request.getName())
                .email(request.getEmail())
                .age(request.getAge())
                .build();

        Student saved = studentRepository.save(student);
        return toResponse(saved);
    }
}

// read one student

public StudentResponse getById(Long id){
    Student student = findOrThrow(id);
    return toResponse(student);
}

//read all students - pagination enforced
public <StudentResponse> getAll(int page, int size, String sortBy){
    Pageable pageable = PageRequest.of(page,size, Sort.by(Sort.Direction.ASC,sortBy));
    return  studentRepository.findAll(pageable).map(this::toResponse);
}

//search
public Page<StudentResponse> search(String keyword, int page, int size){
    Pageable pageable = PageRequest.of(page,size.Sort.by("name"));
    return studentRepository.search(keyword, pageable).map(this::toResponse);
}

//updated the student

public StudentResponse update(Long id, StudentRequest request) {
    Student student = findOrThrow(id);

    student.setName(request.getName());
    student.setEmail(request.getEmail());
    student.setAge(request.getAge());

    Student updatedStudent = studentRepository.save(student);
    return toResponse(updatedStudent);

}

//delete a student

public void delete(Long id){
    findOrThrow(id);
    studentRepository.deleteById(id);
}


//helprs
private Student findOrThrow(Long id) {
    return studentRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(
                    "Student not found with id: " + id));
}

// Maps Entity → ResponseDTO  (never expose the entity directly)
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