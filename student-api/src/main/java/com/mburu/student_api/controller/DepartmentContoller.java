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

    @Operation(summary = "Create a new department", description = "Requires ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Department created"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Department already exists")
    })
    @PostMapping
    public ResponseEntity<DepartmentResponse> create (@Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(departmentService(request));
    }

    @Operation(summary = "Get a department with its ID", description = "Requires USER or ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department retrieved"),
            @ApiResponse(responseCode = "404", description = "Department does not exists")
    })
    @GetMapping("{/id}")
    public ResponseEntity<DepartmentResponse> getById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(getById(id));
    }

    @Operation(summary = "Retrieve all departments", description = "Paginated, Requires USER or ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Departments retrieved"),
            @ApiResponse(responseCode = "404", description = "No Departments Found")
    })
    @GetMapping
    public ResponseEntity<Page<DepartmentResponse>> getAll(

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size,

            @RequestParam(defaultValue = "name")
            String sortBy){
        return ResponseEntity.ok(departmentService.getAll(page,size,sortBy));
    }

    @Operation(summary = "Update a department", description = "Requires ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Department updated"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "404", description = "Department does not exist")
    })
    @PutMapping("{/id}")
    public ResponseEntity<DepartmentResponse> update(@PathVariable Long id, @Valid @RequestBody DepartmentRequest request) {
        return ResponseEntity.ok(departmentService.update(id, request));
    }


    @Operation(summary = "Delete a department", description = "Requires ADMIN role")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No content. Department deleted"),
            @ApiResponse(responseCode = "404", description = "Department does not exist")
    })
    @DeleteMapping("{/id}")
    public ResponseEntity <Void> delete( @PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }


}
