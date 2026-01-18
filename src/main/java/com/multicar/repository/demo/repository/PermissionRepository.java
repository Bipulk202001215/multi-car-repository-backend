package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.PermissionEntity;
import com.multicar.repository.demo.enums.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, String> {
    
    Optional<PermissionEntity> findByPermissionId(String permissionId);
    
    Optional<PermissionEntity> findByPermissionName(Permission permissionName);
    
    boolean existsByPermissionName(Permission permissionName);
}









