package com.mburu.student_api.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotNull(message = "Please enter your name")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Please enter your username")
    @NotBlank(message = "Username cannot be blank")
    private String username;

    @NotBlank(message = "Email must be provided")
    @Email("Please enter a valid email address")
    private String email;

    @NotNull(message = "Enter your password")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 30, message = "Password should not be less than 6 chars.")
    private String
}