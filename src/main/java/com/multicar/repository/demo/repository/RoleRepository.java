package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.RoleEntity;
import com.multicar.repository.demo.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, String> {
    
    Optional<RoleEntity> findByRoleId(String roleId);
    
    Optional<RoleEntity> findByRoleName(UserRole roleName);
    
    boolean existsByRoleName(UserRole roleName);
}





