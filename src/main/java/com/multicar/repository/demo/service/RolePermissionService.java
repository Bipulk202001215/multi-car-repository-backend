package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.PermissionEntity;
import com.multicar.repository.demo.entity.RoleEntity;
import com.multicar.repository.demo.entity.RolePermissionEntity;
import com.multicar.repository.demo.model.Permission;
import com.multicar.repository.demo.model.Role;
import com.multicar.repository.demo.model.RolePermission;
import com.multicar.repository.demo.repository.PermissionRepository;
import com.multicar.repository.demo.repository.RolePermissionRepository;
import com.multicar.repository.demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RolePermission createRolePermission(RolePermission rolePermission) {
        RoleEntity roleEntity = roleRepository.findByRoleId(rolePermission.getRoleId().getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + rolePermission.getRoleId().getRoleId()));
        
        PermissionEntity permissionEntity = permissionRepository.findByPermissionId(rolePermission.getPermission().getPermissionId())
                .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + rolePermission.getPermission().getPermissionId()));
        
        RolePermissionEntity entity = RolePermissionEntity.builder()
                .roleId(roleEntity)
                .permission(permissionEntity)
                .build();
        
        RolePermissionEntity savedEntity = rolePermissionRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public Optional<RolePermission> getRolePermissionById(String rolePermissionId) {
        return rolePermissionRepository.findByRolePermissionId(rolePermissionId)
                .map(this::convertToModel);
    }

    public List<RolePermission> getAllRolePermissions() {
        return rolePermissionRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<RolePermission> updateRolePermission(String rolePermissionId, RolePermission rolePermission) {
        return rolePermissionRepository.findByRolePermissionId(rolePermissionId)
                .map(existingEntity -> {
                    RoleEntity roleEntity = roleRepository.findByRoleId(rolePermission.getRoleId().getRoleId())
                            .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + rolePermission.getRoleId().getRoleId()));
                    
                    PermissionEntity permissionEntity = permissionRepository.findByPermissionId(rolePermission.getPermission().getPermissionId())
                            .orElseThrow(() -> new IllegalArgumentException("Permission not found with id: " + rolePermission.getPermission().getPermissionId()));
                    
                    existingEntity.setRoleId(roleEntity);
                    existingEntity.setPermission(permissionEntity);
                    RolePermissionEntity updatedEntity = rolePermissionRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    public boolean deleteRolePermission(String rolePermissionId) {
        Optional<RolePermissionEntity> entity = rolePermissionRepository.findByRolePermissionId(rolePermissionId);
        if (entity.isPresent()) {
            rolePermissionRepository.delete(entity.get());
            return true;
        }
        return false;
    }

    public List<RolePermission> getRolePermissionsByRoleId(String roleId) {
        return rolePermissionRepository.findByRoleId_RoleId(roleId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public List<RolePermission> getRolePermissionsByPermissionId(String permissionId) {
        return rolePermissionRepository.findByPermission_PermissionId(permissionId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public boolean existsByRoleIdAndPermissionId(String roleId, String permissionId) {
        return rolePermissionRepository.existsByRoleId_RoleIdAndPermission_PermissionId(roleId, permissionId);
    }

    private RolePermission convertToModel(RolePermissionEntity entity) {
        Role role = Role.builder()
                .roleId(entity.getRoleId().getRoleId())
                .roleName(entity.getRoleId().getRoleName())
                .createdOn(entity.getRoleId().getCreatedOn())
                .updatedOn(entity.getRoleId().getUpdatedOn())
                .build();
        
        Permission permission = Permission.builder()
                .permissionId(entity.getPermission().getPermissionId())
                .permissionName(entity.getPermission().getPermissionName())
                .createdOn(entity.getPermission().getCreatedOn())
                .updatedOn(entity.getPermission().getUpdatedOn())
                .build();
        
        return RolePermission.builder()
                .rolePermissionId(entity.getRolePermissionId())
                .roleId(role)
                .permission(permission)
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}



