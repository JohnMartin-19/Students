package com.mburu.student_api.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@Builder

public class DepartmentResponse {
    private Long id;
    private String name;
    private String studentName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
