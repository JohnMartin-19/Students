package com.mburu.student_api.repository;

import com.mburu.student_api.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setUp() {
        Student student = Student.builder()
                .name("James Mwangi")
                .email("james@example.com")
                .age(21)
                .build();
        entityManager.persistAndFlush(student);
    }

    @Test
    @DisplayName("Should find student by email")
    void findByEmail_existingEmail_returnsStudent() {
        Optional<Student> found = studentRepository.findByEmail("james@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("James Mwangi");
    }

    @Test
    @DisplayName("Should return empty when email does not exist")
    void findByEmail_nonExistentEmail_returnsEmpty() {
        assertThat(studentRepository.findByEmail("ghost@example.com")).isEmpty();
    }

    @Test
    @DisplayName("Should return true when email exists")
    void existsByEmail_existingEmail_returnsTrue() {
        assertThat(studentRepository.existsByEmail("james@example.com")).isTrue();
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void existsByEmail_nonExistentEmail_returnsFalse() {
        assertThat(studentRepository.existsByEmail("nobody@example.com")).isFalse();
    }

    @Test
    @DisplayName("Should find students matching keyword in name")
    void search_matchingKeyword_returnsResults() {
        Page<Student> results = studentRepository.search("James", PageRequest.of(0, 10));
        assertThat(results.getContent()).hasSize(1);
        assertThat(results.getContent().get(0).getEmail()).isEqualTo("james@example.com");
    }

    @Test
    @DisplayName("Should return empty page when keyword matches nothing")
    void search_noMatch_returnsEmptyPage() {
        assertThat(studentRepository
                .search("xyz_no_match", PageRequest.of(0, 10))
                .getContent()).isEmpty();
    }
}
