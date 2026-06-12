package com.mburu.student_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoginRequest {

    @NotNull(message = "Email is required")
    @Email(message = "You must provide a valid email address")
    private String email;

    @NotNull(message = "Please input your password")
    @NotBlank(message = "Password must be provided")
    private String password;

}