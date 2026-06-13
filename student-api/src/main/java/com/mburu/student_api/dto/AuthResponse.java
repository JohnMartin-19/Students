package com.mburu.student_api.dto;

import lombok.*;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private String name;
    private String role;

}