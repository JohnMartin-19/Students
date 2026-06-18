package com.mburu.student_api.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotye.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class NotificationService {

    private static final Logger log = LoggerFacotry.getLogger(NotificationService.class);

    @Async("emailExecutor")
    public void sendWelcomeEmail(String email, String name) {
        log.info("Sending email...");

        //init  email server delay(JavaMailSender - prod)
        try{
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Async("reportExecutor")
    public CompletableFuture<Void> sendImportSummaryEmail(String adminEmail, int importedCount) {
        log.info("Sending import summary to {} on thread: {}", adminEmail, Thread.getCurrentThread().getName());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return CompletableFuture(null);
        }
    }
}