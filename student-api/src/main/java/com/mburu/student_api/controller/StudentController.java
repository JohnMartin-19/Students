package com.mburu.student_api.controller;

import com.mburu.student_api.dto.StudentRequest;
import com.mburu.student_api.dto.StudentResponse;
import com.mburu.student_api.service.StudentService;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/students")
@Tag(name = "Students", description = "Student management endpoints")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @Operation(summary = "Create a new student", description = "Requires ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Student created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    public ResponseEntity<StudentResponse> create(@Valid @RequestBody StudentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.create(request));
    }

    @Operation(summary = "Get student by ID", description = "Requires USER or ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

    @Operation(summary = "List all students", description = "Paginated, requires USER or ADMIN role")
    @GetMapping
    public ResponseEntity<Page<StudentResponse>> getAll(
            @RequestParam(defaultValue = "0")
            int page,
            @RequestParam(defaultValue = "10")
            int size,
            @RequestParam(defaultValue = "name")
            String sortBy) {
        return ResponseEntity.ok(studentService.getAll(page, size, sortBy));
    }

    @Operation(summary = "Search students by name or email", description = "Paginated, requires USER or ADMIN role")
    @GetMapping("/search")
    public ResponseEntity<Page<StudentResponse>> search(
            @RequestParam                        String keyword,
            @RequestParam(defaultValue = "0")    int page,
            @RequestParam(defaultValue = "10")   int size) {
        return ResponseEntity.ok(studentService.search(keyword, page, size));
    }

    @Operation(summary = "Update a student", description = "Requires ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<StudentResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.update(id, request));
    }

    @Operation(summary = "Delete a student", description = "Requires ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Student deleted"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
