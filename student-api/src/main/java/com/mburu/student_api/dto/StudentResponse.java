package com.mburu.student_api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
public class StudentResponse {

    private Long id;
    private String name;
    private String email;
    private Integer age;
    private String departmentName;    //---> we expose only the name, not the whole department
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}