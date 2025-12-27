package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.JobDetailEntity;
import com.multicar.repository.demo.model.JobDetail;
import com.multicar.repository.demo.model.JobDescription;
import com.multicar.repository.demo.repository.JobDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class JobDetailService {

    private final JobDetailRepository jobDetailRepository;

    public JobDetail createJobDetail(JobDescription jobDescription) {
        JobDetailEntity entity = JobDetailEntity.builder()
                .jobDescription(jobDescription)
                .build();
        
        JobDetailEntity savedEntity = jobDetailRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public Optional<JobDetail> getJobDetailById(String jobDetailId) {
        return jobDetailRepository.findByJobDetailId(jobDetailId)
                .map(this::convertToModel);
    }

    private JobDetail convertToModel(JobDetailEntity entity) {
        return JobDetail.builder()
                .jobDetailId(entity.getJobDetailId())
                .jobDescription(entity.getJobDescription())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}



