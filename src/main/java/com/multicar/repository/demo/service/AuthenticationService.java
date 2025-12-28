package com.multicar.repository.demo.service;

import com.multicar.repository.demo.model.LoginRequest;
import com.multicar.repository.demo.model.LoginResponse;
import com.multicar.repository.demo.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;

    public LoginResponse authenticate(LoginRequest loginRequest) {
        // Validate user credentials
        boolean passwordValid = userService.validateUserPassword(loginRequest.getEmailId(), loginRequest.getPassword());
        
        if (!passwordValid) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Get user by email (after password validation)
        Optional<User> userOptional = userService.getUserByEmailId(loginRequest.getEmailId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        User user = userOptional.get();

        // Extract company ID
        String companyId = user.getCompanyId() != null ? user.getCompanyId().getCompanyId() : null;

        // Generate JWT token
        String token = jwtService.generateToken(
                user.getUserId(),
                user.getEmailId(),
                user.getUserType().name(),
                companyId
        );

        // Build and return login response
        return LoginResponse.builder()
                .token(token)
                .userId(user.getUserId())
                .emailId(user.getEmailId())
                .userType(user.getUserType())
                .companyId(user.getCompanyId())
                .build();
    }
}

