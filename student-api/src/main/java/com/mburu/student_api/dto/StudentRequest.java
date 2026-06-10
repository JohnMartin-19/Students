package com.mburu.student_api.dto;

import lombok.Data;

@Data
public class StudentRequest {
    private String name;
    private String email;
    private Integer age;
    private Long departmentId;
}
