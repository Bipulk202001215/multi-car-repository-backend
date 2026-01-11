package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.JobCardEntity;
import com.multicar.repository.demo.enums.JobStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobCardRepository extends JpaRepository<JobCardEntity, String> {
    
    Optional<JobCardEntity> findByJobCardId(String jobCardId);
    
    Optional<JobCardEntity> findByVehicleNumber(String vehicleNumber);
    
    List<JobCardEntity> findByCompanyId(String companyId);
    
    List<JobCardEntity> findByStatus(JobStatus status);
}



