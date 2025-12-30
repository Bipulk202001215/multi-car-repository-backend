package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.SoldInventoryItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SoldInventoryItemRepository extends JpaRepository<SoldInventoryItemEntity, String> {
    
    Optional<SoldInventoryItemEntity> findBySoldInventoryId(String soldInventoryId);
    
    List<SoldInventoryItemEntity> findByInventoryId_InventoryId(String inventoryId);
    
    List<SoldInventoryItemEntity> findByJobId(String jobId);
}





