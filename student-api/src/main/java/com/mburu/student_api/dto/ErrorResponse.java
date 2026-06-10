package com.mburu.student_api.dto;

import lombok.*;
import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String errorCode;
    private String message;
    private String path;
    private Map<String, String> fieldErrors;
}