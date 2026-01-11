package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
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
        JobCard jobCard = jobCardService.getJobCardById(jobCardId)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + jobCardId, ErrorCode.JOB_NOT_FOUND));
        return ResponseEntity.ok(jobCard);
    }

    @GetMapping
    public ResponseEntity<List<JobCard>> getAllJobCards() {
        List<JobCard> jobCards = jobCardService.getAllJobCards();
        return ResponseEntity.ok(jobCards);
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<JobCard>> getJobCardsByCompanyId(@PathVariable String companyId) {
        List<JobCard> jobCards = jobCardService.getJobCardsByCompanyId(companyId);
        return ResponseEntity.ok(jobCards);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<JobCard>> getPendingJobs() {
        List<JobCard> pendingJobs = jobCardService.getPendingJobs();
        return ResponseEntity.ok(pendingJobs);
    }

    @PutMapping("/{jobCardId}")
    public ResponseEntity<JobCard> updateJobCard(
            @PathVariable String jobCardId,
            @RequestBody CreateJobRequest request) {
        JobCard updatedJobCard = jobCardService.updateJobCard(jobCardId, request)
                .orElseThrow(() -> new ResourceNotFoundException("Job card not found with id: " + jobCardId, ErrorCode.JOB_NOT_FOUND));
        return ResponseEntity.ok(updatedJobCard);
    }

    @DeleteMapping("/{jobCardId}")
    public ResponseEntity<Void> deleteJobCard(@PathVariable String jobCardId) {
        boolean deleted = jobCardService.deleteJobCard(jobCardId);
        if (!deleted) {
            throw new ResourceNotFoundException("Job card not found with id: " + jobCardId, ErrorCode.JOB_NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }
}



