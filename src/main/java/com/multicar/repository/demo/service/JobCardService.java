package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.JobCardEntity;
import com.multicar.repository.demo.entity.JobDetailEntity;
import com.multicar.repository.demo.enums.JobStatus;
import com.multicar.repository.demo.model.CreateJobRequest;
import com.multicar.repository.demo.model.JobCard;
import com.multicar.repository.demo.repository.JobCardRepository;
import com.multicar.repository.demo.repository.JobDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class JobCardService {

    private final JobCardRepository jobCardRepository;
    private final JobDetailRepository jobDetailRepository;

    public JobCard createJob(CreateJobRequest request) {
        // Create ONE JobDetailEntity with the entire list of job descriptions
        // The entire List<JobDescription> will be stored as JSON in a single JobDetailEntity record
        JobDetailEntity jobDetailEntity = JobDetailEntity.builder()
                .jobDescription(request.getJobDescription()) // Stores entire list as JSON
                .build();
        
        JobDetailEntity savedJobDetail = jobDetailRepository.save(jobDetailEntity);
        
        // Create JobCardEntity with reference to JobDetailEntity
        JobCardEntity jobCardEntity = JobCardEntity.builder()
                .jobDetailId(savedJobDetail)
                .vehicleNumber(request.getVehicleNumber())
                .kmReading(request.getKmReading())
                .jobDate(request.getJobDate())
                .status(request.getStatus() != null ? request.getStatus() : JobStatus.PENDING)
                .checkinTime(request.getCheckinTime())
                .mobileNumber(request.getMobileNumber())
                .estimatedDelivery(request.getEstimatedDelivery())
                .companyId(request.getCompanyId())
                .invoiceId(null) // invoice_id is not part of request, set to null initially
                .build();
        
        JobCardEntity savedJobCard = jobCardRepository.save(jobCardEntity);
        return convertToModel(savedJobCard);
    }

    public Optional<JobCard> getJobCardById(String jobCardId) {
        return jobCardRepository.findByJobCardId(jobCardId)
                .map(this::convertToModel);
    }

    public List<JobCard> getAllJobCards() {
        return jobCardRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<JobCard> updateJobCard(String jobCardId, CreateJobRequest request) {
        return jobCardRepository.findByJobCardId(jobCardId)
                .map(existingEntity -> {
                    existingEntity.setVehicleNumber(request.getVehicleNumber());
                    existingEntity.setKmReading(request.getKmReading());
                    existingEntity.setJobDate(request.getJobDate());
                    if (request.getStatus() != null) {
                        existingEntity.setStatus(request.getStatus());
                    }
                    existingEntity.setCheckinTime(request.getCheckinTime());
                    existingEntity.setEstimatedDelivery(request.getEstimatedDelivery());
                    // invoice_id is not updated from request, keep existing value
                    
                    // Always maintain/update jobDescription
                    if (request.getJobDescription() != null) {
                        JobDetailEntity jobDetail = existingEntity.getJobDetailId();
                        if (jobDetail != null) {
                            // Update existing job detail
                            jobDetail.setJobDescription(request.getJobDescription());
                            jobDetailRepository.save(jobDetail);
                        } else {
                            // Create new job detail if it doesn't exist
                            JobDetailEntity newJobDetail = JobDetailEntity.builder()
                                    .jobDescription(request.getJobDescription())
                                    .build();
                            JobDetailEntity savedJobDetail = jobDetailRepository.save(newJobDetail);
                            existingEntity.setJobDetailId(savedJobDetail);
                        }
                    }
                    
                    JobCardEntity updatedEntity = jobCardRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    public boolean deleteJobCard(String jobCardId) {
        Optional<JobCardEntity> entity = jobCardRepository.findByJobCardId(jobCardId);
        if (entity.isPresent()) {
            jobCardRepository.delete(entity.get());
            return true;
        }
        return false;
    }

    private JobCard convertToModel(JobCardEntity entity) {
        return JobCard.builder()
                .jobCardId(entity.getJobCardId())
                .jobDetailId(convertJobDetailToModel(entity.getJobDetailId()))
                .vehicleNumber(entity.getVehicleNumber())
                .kmReading(entity.getKmReading())
                .jobDate(entity.getJobDate())
                .status(entity.getStatus())
                .checkinTime(entity.getCheckinTime())
                .estimatedDelivery(entity.getEstimatedDelivery())
                .invoiceId(entity.getInvoiceId())
                .createdOn(entity.getCreatedOn())
                .mobileNumber(entity.getMobileNumber())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }

    private com.multicar.repository.demo.model.JobDetail convertJobDetailToModel(JobDetailEntity entity) {
        if (entity == null) {
            return null;
        }
        return com.multicar.repository.demo.model.JobDetail.builder()
                .jobDetailId(entity.getJobDetailId())
                .jobDescription(entity.getJobDescription())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}

