package com.mburu.student_api.service;

import com.mburu.student_api.dto.StudentRequest;
import com.mburu.student_api.dto.StudentResponse;
import com.mburu.student_api.entity.Student;
import com.mburu.student_api.exception.DuplicateResourceException;
import com.mburu.student_api.exception.ResourceNotFoundException;
import com.mburu.student_api.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student student;
    private StudentRequest request;

    @BeforeEach
    void setUp() {
        student = Student.builder()
                .id(1L)
                .name("James Mwangi")
                .email("james@example.com")
                .age(21)
                .build();

        request = new StudentRequest();
        request.setName("James Mwangi");
        request.setEmail("james@example.com");
        request.setAge(21);
    }

    @Test
    @DisplayName("Should create student when email is not taken")
    void create_newEmail_returnsStudentResponse() {
        when(studentRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        StudentResponse response = studentService.create(request);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("James Mwangi");
        assertThat(response.getEmail()).isEqualTo("james@example.com");
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException when email already exists")
    void create_duplicateEmail_throwsDuplicateResourceException() {
        when(studentRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> studentService.create(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("james@example.com");

        verify(studentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should return student when valid ID is provided")
    void getById_existingId_returnsStudentResponse() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        StudentResponse response = studentService.getById(1L);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("James Mwangi");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when ID does not exist")
    void getById_nonExistentId_throwsResourceNotFoundException() {
        // matches what StudentService.findOrThrow() ACTUALLY throws
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.getById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    @DisplayName("Should update student fields when student exists")
    void update_existingStudent_returnsUpdatedResponse() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        request.setName("James Kariuki");
        request.setAge(22);

        StudentResponse response = studentService.update(1L, request);

        assertThat(response).isNotNull();
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    @Test
    @DisplayName("Should delete student when ID exists")
    void delete_existingId_deletesSuccessfully() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        studentService.delete(1L);

        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when deleting non-existent student")
    void delete_nonExistentId_throwsResourceNotFoundException() {
        // matches what StudentService.findOrThrow() ACTUALLY throws
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studentService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(studentRepository, never()).deleteById(any());
    }
}
