package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<SupplierEntity, String> {
    
    Optional<SupplierEntity> findBySupplierId(String supplierId);
    
    Optional<SupplierEntity> findByGstin(String gstin);
    
    List<SupplierEntity> findByCompanyId(String companyId);
}









