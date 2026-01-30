package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.model.HealthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/health")
@RequiredArgsConstructor
public class HealthController {

    private final DataSource dataSource;

    @Value("${spring.application.name:Multi Car Repair System}")
    private String applicationName;

    @GetMapping
    public ResponseEntity<HealthResponse> healthCheck() {
        String appStatus = "UP";
        String dbStatus = "DOWN";

        // Check database connectivity
        try (Connection connection = dataSource.getConnection()) {
            if (connection != null && !connection.isClosed()) {
                dbStatus = "UP";
            }
        } catch (Exception e) {
            dbStatus = "DOWN";
        }

        HealthResponse healthResponse = HealthResponse.builder()
                .status(appStatus)
                .database(dbStatus)
                .timestamp(LocalDateTime.now())
                .application(applicationName)
                .build();

        return ResponseEntity.ok(healthResponse);
    }
}
