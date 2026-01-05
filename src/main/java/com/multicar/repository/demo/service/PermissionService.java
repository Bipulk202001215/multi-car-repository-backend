package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.PermissionEntity;
import com.multicar.repository.demo.model.Permission;
import com.multicar.repository.demo.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public Permission createPermission(Permission permission) {
        PermissionEntity entity = PermissionEntity.builder()
                .permissionName(permission.getPermissionName())
                .build();
        
        PermissionEntity savedEntity = permissionRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public Optional<Permission> getPermissionById(String permissionId) {
        return permissionRepository.findByPermissionId(permissionId)
                .map(this::convertToModel);
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<Permission> updatePermission(String permissionId, Permission permission) {
        return permissionRepository.findByPermissionId(permissionId)
                .map(existingEntity -> {
                    existingEntity.setPermissionName(permission.getPermissionName());
                    PermissionEntity updatedEntity = permissionRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    public boolean deletePermission(String permissionId) {
        Optional<PermissionEntity> entity = permissionRepository.findByPermissionId(permissionId);
        if (entity.isPresent()) {
            permissionRepository.delete(entity.get());
            return true;
        }
        return false;
    }

    public Optional<Permission> getPermissionByName(com.multicar.repository.demo.enums.Permission permissionName) {
        return permissionRepository.findByPermissionName(permissionName)
                .map(this::convertToModel);
    }

    public boolean existsByPermissionName(com.multicar.repository.demo.enums.Permission permissionName) {
        return permissionRepository.existsByPermissionName(permissionName);
    }

    private Permission convertToModel(PermissionEntity entity) {
        return Permission.builder()
                .permissionId(entity.getPermissionId())
                .permissionName(entity.getPermissionName())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}








