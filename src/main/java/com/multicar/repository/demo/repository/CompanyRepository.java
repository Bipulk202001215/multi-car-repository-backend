package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyEntity, String> {
    
    Optional<CompanyEntity> findByCompanyId(String companyId);
    
    Optional<CompanyEntity> findByGstin(String gstin);
    
    boolean existsByGstin(String gstin);
}








