package com.mburu.student_api.controller;

import com.mburu.student_api.dto.DepartmentRequest;
import com.mburu.student_api.dto.DepartmentResponse;
import com.mburu.student_api.service.DepartmentService;
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
@RquestMapping("/api/students")
@Tag(name = "Departments", description = "Dept management endpoints")

public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<DepartmentResponse> create (@Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService(request));
    }

    @GetMapping("{/id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(getById(id));
    }
}
