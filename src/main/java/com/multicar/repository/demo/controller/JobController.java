package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.model.CreateJobRequest;
import com.multicar.repository.demo.model.JobCard;
import com.multicar.repository.demo.service.JobCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobController {

    private final JobCardService jobCardService;

    @PostMapping
    public ResponseEntity<JobCard> createJob(@RequestBody CreateJobRequest request) {
        JobCard createdJob = jobCardService.createJob(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdJob);
    }

    @GetMapping("/{jobCardId}")
    public ResponseEntity<JobCard> getJobCardById(@PathVariable String jobCardId) {
        return jobCardService.getJobCardById(jobCardId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<JobCard>> getAllJobCards() {
        List<JobCard> jobCards = jobCardService.getAllJobCards();
        return ResponseEntity.ok(jobCards);
    }

    @PutMapping("/{jobCardId}")
    public ResponseEntity<JobCard> updateJobCard(
            @PathVariable String jobCardId,
            @RequestBody CreateJobRequest request) {
        return jobCardService.updateJobCard(jobCardId, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{jobCardId}")
    public ResponseEntity<Void> deleteJobCard(@PathVariable String jobCardId) {
        boolean deleted = jobCardService.deleteJobCard(jobCardId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}



