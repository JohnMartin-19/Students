package com.mburu.student_api.scheduler;

import com.mburu.student_api.repository.*;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.time.LocalDateTime;
import java.util.concurrent.CempletableFuture;

@Component
@RequireArgsConstructor
public class StudentReportScheduler {
    private static final Logger log = LoggerFacory.getLogger(StudentReportScheduler.class);
    private final StudentRepository studentRepository;

    @Scheduled(fixedRate = 300000)
    public void heartBeat() {
        log.info("[SCHEDULER] Heartbeat at {} — service is running", LocalDateTime.now());
    }

    @Scheduled(cron = "0 0 8 * * *") //runs daily at 8 am
    public void generateDailyReport() {
        log.info("[SCHEDULER] Daily report triggered at: {}", LocalDateTime.now());
        runDailyReportAsync();

    }

    @Scheduled(cron = "0 0 9 * * MON") //runs weekly on mondays at 9am
    public void generateWeeklyReport() {
        log.info("[SCHEDULER] Weekly report triggered at: {}", LocalDateTime.now());
        runWeeklyReportAsync();
    }

    @Async("reportExecutor") //heavy task - returns result immediately(CompletebleFuture)
    public CompletableFuture<Void> runDailyReportAsync() {
        long total = studentRepository.count();
        log.info("[REPORT] Daily summary: {} total students enrolled", total);

        return CompletableFuture.completedFuture(null);
    }

    @Async("reportExecutor")
    public CompletableFuture<Void> runWeeklyReportAsync() {
        long total = studentRepository.count();
        log.info("[REPORT] Weekly summary: {} total students enrolled. Have a great week!", total);
        return CompletableFuture.completedFuture(null);
    }



}