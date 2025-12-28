package com.multicar.repository.demo.service;

import com.multicar.repository.demo.entity.CompanyEntity;
import com.multicar.repository.demo.entity.UserEntity;
import com.multicar.repository.demo.enums.UserType;
import com.multicar.repository.demo.model.Company;
import com.multicar.repository.demo.model.User;
import com.multicar.repository.demo.repository.CompanyRepository;
import com.multicar.repository.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public User createUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmailId(user.getEmailId())) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmailId());
        }
        
        // Validate password is provided
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        
        // Fetch company if provided
        CompanyEntity companyEntity = null;
        if (user.getCompanyId() != null && user.getCompanyId().getCompanyId() != null) {
            companyEntity = companyRepository.findByCompanyId(user.getCompanyId().getCompanyId())
                    .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + user.getCompanyId().getCompanyId()));
        }
        
        // Encrypt password
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        
        UserEntity entity = UserEntity.builder()
                .emailId(user.getEmailId())
                .password(encryptedPassword)
                .companyId(companyEntity)
                .userType(user.getUserType())
                .build();
        
        UserEntity savedEntity = userRepository.save(entity);
        return convertToModel(savedEntity);
    }

    public Optional<User> getUserById(String userId) {
        return userRepository.findByUserId(userId)
                .map(this::convertToModel);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public Optional<User> updateUser(String userId, User user) {
        return userRepository.findByUserId(userId)
                .map(existingEntity -> {
                    // Check if email is being changed and if new email already exists
                    if (!existingEntity.getEmailId().equals(user.getEmailId()) && 
                        userRepository.existsByEmailId(user.getEmailId())) {
                        throw new IllegalArgumentException("Email already exists: " + user.getEmailId());
                    }
                    
                    existingEntity.setEmailId(user.getEmailId());
                    
                    // Update password only if provided
                    if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                        String encryptedPassword = passwordEncoder.encode(user.getPassword());
                        existingEntity.setPassword(encryptedPassword);
                    }
                    
                    // Update company if provided
                    if (user.getCompanyId() != null && user.getCompanyId().getCompanyId() != null) {
                        CompanyEntity companyEntity = companyRepository.findByCompanyId(user.getCompanyId().getCompanyId())
                                .orElseThrow(() -> new IllegalArgumentException("Company not found with id: " + user.getCompanyId().getCompanyId()));
                        existingEntity.setCompanyId(companyEntity);
                    } else {
                        existingEntity.setCompanyId(null);
                    }
                    
                    existingEntity.setUserType(user.getUserType());
                    
                    UserEntity updatedEntity = userRepository.save(existingEntity);
                    return convertToModel(updatedEntity);
                });
    }

    public boolean deleteUser(String userId) {
        Optional<UserEntity> entity = userRepository.findByUserId(userId);
        if (entity.isPresent()) {
            userRepository.delete(entity.get());
            return true;
        }
        return false;
    }

    public Optional<User> getUserByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId)
                .map(this::convertToModel);
    }

    public List<User> getUsersByCompanyId(String companyId) {
        return userRepository.findByCompanyId_CompanyId(companyId).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public List<User> getUsersByUserType(UserType userType) {
        return userRepository.findByUserType(userType).stream()
                .map(this::convertToModel)
                .collect(Collectors.toList());
    }

    public boolean existsByEmailId(String emailId) {
        return userRepository.existsByEmailId(emailId);
    }

    public boolean validateUserPassword(String emailId, String rawPassword) {
        Optional<UserEntity> userEntityOptional = userRepository.findByEmailId(emailId);
        if (userEntityOptional.isEmpty()) {
            return false;
        }
        UserEntity userEntity = userEntityOptional.get();
        return passwordEncoder.matches(rawPassword, userEntity.getPassword());
    }

    private User convertToModel(UserEntity entity) {
        Company company = null;
        if (entity.getCompanyId() != null) {
            company = Company.builder()
                    .companyId(entity.getCompanyId().getCompanyId())
                    .companyName(entity.getCompanyId().getCompanyName())
                    .gstIn(entity.getCompanyId().getGstin())
                    .createdOn(entity.getCompanyId().getCreatedOn())
                    .updatedOn(entity.getCompanyId().getUpdatedOn())
                    .build();
        }
        
        return User.builder()
                .userId(entity.getUserId())
                .emailId(entity.getEmailId())
                .companyId(company)
                .userType(entity.getUserType())
                .createdOn(entity.getCreatedOn())
                .updatedOn(entity.getUpdatedOn())
                .build();
    }
}

