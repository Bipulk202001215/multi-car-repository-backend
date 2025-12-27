package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {
    
    Optional<UserRoleEntity> findByUserRoleId(String userRoleId);
    
    List<UserRoleEntity> findByUserId_UserId(String userId);
    
    List<UserRoleEntity> findByRoleId_RoleId(String roleId);
    
    boolean existsByUserId_UserIdAndRoleId_RoleId(String userId, String roleId);
}



