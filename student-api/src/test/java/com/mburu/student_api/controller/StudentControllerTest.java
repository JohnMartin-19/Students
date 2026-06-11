package com.mburu.student_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mburu.student_api.dto.StudentRequest;
import com.mburu.student_api.dto.StudentResponse;
import com.mburu.student_api.exception.DuplicateResourceException;
import com.mburu.student_api.repository.StudentRepository;
import com.mburu.student_api.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StudentControllerTest {

    @Autowired
    private MockMvc mvc;

    // Mock the REPOSITORY, not the service — lets the real service + controller wire up
    @MockitoBean
    private StudentRepository studentRepository;

    @Autowired
    private StudentService studentService;

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules();

    private com.mburu.student_api.entity.Student student;
    private StudentResponse studentResponse;
    private StudentRequest studentRequest;

    @BeforeEach
    void setUp() {
        student = com.mburu.student_api.entity.Student.builder()
                .id(1L)
                .name("James Mwangi")
                .email("james@example.com")
                .age(21)
                .build();

        studentResponse = StudentResponse.builder()
                .id(1L)
                .name("James Mwangi")
                .email("james@example.com")
                .age(21)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        studentRequest = new StudentRequest();
        studentRequest.setName("James Mwangi");
        studentRequest.setEmail("james@example.com");
        studentRequest.setAge(21);
    }

    @Test
    @DisplayName("POST /api/students - should return 201 and student body")
    void create_validRequest_returns201() throws Exception {
        when(studentRepository.existsByEmail("james@example.com")).thenReturn(false);
        when(studentRepository.save(any())).thenReturn(student);

        mvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("James Mwangi"))
                .andExpect(jsonPath("$.email").value("james@example.com"));
    }

    @Test
    @DisplayName("POST /api/students - should return 400 when name is blank")
    void create_blankName_returns400() throws Exception {
        studentRequest.setName("");

        mvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.fieldErrors.name").exists());
    }

    @Test
    @DisplayName("POST /api/students - should return 409 on duplicate email")
    void create_duplicateEmail_returns409() throws Exception {
        when(studentRepository.existsByEmail("james@example.com")).thenReturn(true);

        mvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value("DUPLICATE_RESOURCE"));
    }

    @Test
    @DisplayName("GET /api/students/1 - should return 200 and student")
    void getById_existingId_returns200() throws Exception {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("James Mwangi"));
    }

    @Test
    @DisplayName("GET /api/students/99 - should return 404 when not found")
    void getById_nonExistentId_returns404() throws Exception {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        mvc.perform(get("/api/students/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value("RESOURCE_NOT_FOUND"));
    }

    @Test
    @DisplayName("GET /api/students - should return paginated list")
    void getAll_returns200WithPage() throws Exception {
        var page = new PageImpl<>(List.of(student), PageRequest.of(0, 10), 1);
        when(studentRepository.findAll(any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        mvc.perform(get("/api/students?page=0&size=10&sortBy=name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("James Mwangi"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("PUT /api/students/1 - should return 200")
    void update_validRequest_returns200() throws Exception {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(studentRepository.save(any())).thenReturn(student);

        mvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @DisplayName("DELETE /api/students/1 - should return 204")
    void delete_existingId_returns204() throws Exception {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        mvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }
}
