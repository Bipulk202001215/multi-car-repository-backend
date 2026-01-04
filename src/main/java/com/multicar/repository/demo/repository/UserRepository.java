package com.multicar.repository.demo.repository;

import com.multicar.repository.demo.entity.UserEntity;
import com.multicar.repository.demo.enums.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    
    Optional<UserEntity> findByUserId(String userId);
    
    Optional<UserEntity> findByEmailId(String emailId);
    
    boolean existsByEmailId(String emailId);
    
    List<UserEntity> findByCompanyId_CompanyId(String companyId);
    
    List<UserEntity> findByUserType(UserType userType);
}







