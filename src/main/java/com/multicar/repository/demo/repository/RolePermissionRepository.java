package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, String> {
    
    Optional<RolePermissionEntity> findByRolePermissionId(String rolePermissionId);
    
    List<RolePermissionEntity> findByRoleId_RoleId(String roleId);
    
    List<RolePermissionEntity> findByPermission_PermissionId(String permissionId);
    
    boolean existsByRoleId_RoleIdAndPermission_PermissionId(String roleId, String permissionId);
}









