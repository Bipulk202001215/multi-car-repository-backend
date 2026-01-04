package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.JobDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobDetailRepository extends JpaRepository<JobDetailEntity, String> {
    
    Optional<JobDetailEntity> findByJobDetailId(String jobDetailId);
}







