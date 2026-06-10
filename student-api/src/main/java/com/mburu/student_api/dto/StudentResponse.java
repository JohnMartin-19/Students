package com.mburu.student_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentResponse {
    private Long id;
    private String name;
    private String email;
    private Integer age;
    private String departmentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
