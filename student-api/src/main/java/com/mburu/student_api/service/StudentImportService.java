package com.mburu.student_api.service;

import com.mburu.student_api.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframewor.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletebleFuture;

@Service
@RequiredArgsConstructor
public class StudentImportService {

    private static final Logger log = LoggerFactory.getLogger(StudentImportService.class);

    private final StudentRepository studentRepository;
    private final NotificationService notificationService;

    @Async("reportExecutor")
    public CompletableFuture<Integer> importStudents(List<StudentRequest> students, String adminEmail) {
        log.info("[IMPORT] Starting async import of {} students on thread: {}",
                students.size(), Thread.currentThread().getName());

        List<Student> toSave = new ArrayList<>();
        int skipped = 0;

        for (StudentRequest req : students) {
            //skip duplicates silently during bulk import
            if (studentRepository.existsByEmail(req.getEmail())) {
                log.warn("[IMPORT] Skipping duplicate email: {}", req.getEmail());
                skipped++;
                continue;
            }
            toSave.add(Student.builder()
                    .name(req.getName())
                    .email(req.getEmail())
                    .age(req.getAge())
                    .build());
        }

        List<Student> saved = studentRepository.saveAll(toSave);
        int importedCount = saved.size();

        log.info("[IMPORT] Import complete — saved: {}, skipped: {}", importedCount, skipped);

        // alert admin asynchronously (runs on email thread pool)
        notificationService.sendImportSummaryEmail(adminEmail, importedCount);

        return CompletableFuture.completedFuture(importedCount);
    }
}
