package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.RoleEntity;
import com.multicar.repository.demo.entity.UserEntity;
import com.multicar.repository.demo.entity.UserRoleEntity;
import com.multicar.repository.demo.model.Role;
import com.multicar.repository.demo.model.User;
import com.multicar.repository.demo.model.UserRole;
import com.multicar.repository.demo.repository.RoleRepository;
import com.multicar.repository.demo.repository.UserRepository;
import com.multicar.repository.demo.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserRole createUserRole(UserRole userRole) {
        // Check if user-role combination already exists
        if (userRoleRepository.existsByUserId_UserIdAndRoleId_RoleId(
                userRole.getUserId().getUserId(),
                userRole.getRoleId().getRoleId())) {
            throw new IllegalArgumentException("User-Role combination already exists");
        }
        
        UserEntity userEntity = userRepository.findByUserId(userRole.getUserId().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userRole.getUserId().getUserId()));
        
        RoleEntity roleEntity = roleRepository.findByRoleId(userRole.getRoleId().getRoleId())
                .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + userRole.getRoleId().getRoleId()));
        
        UserRoleEntity entity = UserRoleEntity.builder()
                .userId(userEntity)
                .roleId(roleEntity)
                .build();
        
        UserRoleEntity savedEntity = userRoleRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public Optional<UserRole> getUserRoleById(String userRoleId) {
        return userRoleRepository.findByUserRoleId(userRoleId)
                .map(this::convertToModel);
    }

    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<UserRole> updateUserRole(String userRoleId, UserRole userRole) {
        return userRoleRepository.findByUserRoleId(userRoleId)
                .map(existingEntity -> {
                    // Check if new user-role combination already exists (excluding current record)
                    String newUserId = userRole.getUserId().getUserId();
                    String newRoleId = userRole.getRoleId().getRoleId();
                    
                    if (!existingEntity.getUserId().getUserId().equals(newUserId) || 
                        !existingEntity.getRoleId().getRoleId().equals(newRoleId)) {
                        if (userRoleRepository.existsByUserId_UserIdAndRoleId_RoleId(newUserId, newRoleId)) {
                            throw new IllegalArgumentException("User-Role combination already exists");
                        }
                    }
                    
                    UserEntity userEntity = userRepository.findByUserId(newUserId)
                            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + newUserId));
                    
                    RoleEntity roleEntity = roleRepository.findByRoleId(newRoleId)
                            .orElseThrow(() -> new IllegalArgumentException("Role not found with id: " + newRoleId));
                    
                    existingEntity.setUserId(userEntity);
                    existingEntity.setRoleId(roleEntity);
                    UserRoleEntity updatedEntity = userRoleRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    public boolean deleteUserRole(String userRoleId) {
        Optional<UserRoleEntity> entity = userRoleRepository.findByUserRoleId(userRoleId);
        if (entity.isPresent()) {
            userRoleRepository.delete(entity.get());
            return true;
        }
        return false;
    }

    public List<UserRole> getUserRolesByUserId(String userId) {
        return userRoleRepository.findByUserId_UserId(userId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public List<UserRole> getUserRolesByRoleId(String roleId) {
        return userRoleRepository.findByRoleId_RoleId(roleId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public boolean existsByUserIdAndRoleId(String userId, String roleId) {
        return userRoleRepository.existsByUserId_UserIdAndRoleId_RoleId(userId, roleId);
    }

    private UserRole convertToModel(UserRoleEntity entity) {
        User user = User.builder()
                .userId(entity.getUserId().getUserId())
                .emailId(entity.getUserId().getEmailId())
                .userType(entity.getUserId().getUserType())
                .createdOn(entity.getUserId().getCreatedOn())
                .updatedOn(entity.getUserId().getUpdatedOn())
                .build();
        
        Role role = Role.builder()
                .roleId(entity.getRoleId().getRoleId())
                .roleName(entity.getRoleId().getRoleName())
                .createdOn(entity.getRoleId().getCreatedOn())
                .updatedOn(entity.getRoleId().getUpdatedOn())
                .build();
        
        return UserRole.builder()
                .userRoleId(entity.getUserRoleId())
                .userId(user)
                .roleId(role)
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}






