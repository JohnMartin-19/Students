package com.mburu.student_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annoataion.EnableScheduling;


@SpringBootApplication
@EnableAsync
@EnableScheduling
public class StudentApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudentApiApplication.class, args);
    }
}
