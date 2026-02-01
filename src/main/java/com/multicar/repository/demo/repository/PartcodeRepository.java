package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.PartcodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PartcodeRepository extends JpaRepository<PartcodeEntity, String> {
    
    Optional<PartcodeEntity> findByPartCode(String partCode);
    
    Optional<PartcodeEntity> findByPartCodeAndCompanyId(String partCode, String companyId);
    
    List<PartcodeEntity> findByCompanyId(String companyId);
    
    @Query("SELECT p FROM PartcodeEntity p WHERE p.units <= p.minStockAlert")
    List<PartcodeEntity> findLowStockItems();
    
    @Query("SELECT p FROM PartcodeEntity p WHERE p.units <= p.minStockAlert AND p.companyId = :companyId")
    List<PartcodeEntity> findLowStockItemsByCompanyId(String companyId);
}





