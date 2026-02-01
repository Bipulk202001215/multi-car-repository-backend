package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.InventoryEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryEventRepository extends JpaRepository<InventoryEventEntity, String> {
    
    List<InventoryEventEntity> findByPartCode(String partCode);
    
    List<InventoryEventEntity> findByPartCodeAndCompanyId(String partCode, String companyId);
    
    List<InventoryEventEntity> findByJobId(String jobId);
    
    List<InventoryEventEntity> findByCompanyId(String companyId);
}





