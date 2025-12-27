package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<InventoryEntity, String> {
    
    Optional<InventoryEntity> findByInventoryId(String inventoryId);
    
    Optional<InventoryEntity> findByPartCode(String partCode);
    
    List<InventoryEntity> findByCompanyId(String companyId);
    
    List<InventoryEntity> findBySupplierId(String supplierId);
    
    @Query("SELECT i FROM InventoryEntity i WHERE i.remainingUnits <= i.minStockAlert")
    List<InventoryEntity> findLowStockItems();
    
    @Query("SELECT i FROM InventoryEntity i WHERE i.companyId = :companyId AND i.remainingUnits <= i.minStockAlert")
    List<InventoryEntity> findLowStockItemsByCompanyId(@Param("companyId") String companyId);
}



