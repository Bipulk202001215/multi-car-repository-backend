package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.RoleEntity;
import com.multicar.repository.demo.model.Role;
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
public class RoleService {

    private final RoleRepository roleRepository;

    public Role createRole(Role role) {
        RoleEntity entity = RoleEntity.builder()
                .roleName(role.getRoleName())
                .build();
        
        RoleEntity savedEntity = roleRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public Optional<Role> getRoleById(String roleId) {
        return roleRepository.findByRoleId(roleId)
                .map(this::convertToModel);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<Role> updateRole(String roleId, Role role) {
        return roleRepository.findByRoleId(roleId)
                .map(existingEntity -> {
                    existingEntity.setRoleName(role.getRoleName());
                    RoleEntity updatedEntity = roleRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    public boolean deleteRole(String roleId) {
        Optional<RoleEntity> entity = roleRepository.findByRoleId(roleId);
        if (entity.isPresent()) {
            roleRepository.delete(entity.get());
            return true;
        }
        return false;
    }

    public Optional<Role> getRoleByName(com.multicar.repository.demo.enums.UserRole roleName) {
        return roleRepository.findByRoleName(roleName)
                .map(this::convertToModel);
    }

    public boolean existsByRoleName(com.multicar.repository.demo.enums.UserRole roleName) {
        return roleRepository.existsByRoleName(roleName);
    }

    private Role convertToModel(RoleEntity entity) {
        return Role.builder()
                .roleId(entity.getRoleId())
                .roleName(entity.getRoleName())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}





