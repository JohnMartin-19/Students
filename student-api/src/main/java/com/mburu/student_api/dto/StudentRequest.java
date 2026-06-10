package com.mburu.student_api.dto;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
//validations are put in the request DTOs -->
// you validate what comes in, not what is being stored
public class StudentRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 chars")
    private String name;

    @NotNull(message = "Email field is required")
    @Email(message = "Must be a valid email address")
    private String email;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Student must be atleast 18 years old")
    @Max(value = 35, message = "Maximum age is 35. Youths only.")
    private Integer age;

    //@NotNull(message = "Depsrtment ID is required")
    @Positive(message = "Department ID cannot be a negative value")
    private Long departmentId;
}
