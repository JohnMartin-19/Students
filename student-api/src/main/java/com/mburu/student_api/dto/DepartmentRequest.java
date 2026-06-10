package com.mburu.student_api.dto;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
public class DepartmentRequest {
    @NotNull(message = "Name cannot be null")
    private String name;

    //@NotNull()
    @Positive(message = "Student ID cannot be a negative value")
    private Long studentId;
}

