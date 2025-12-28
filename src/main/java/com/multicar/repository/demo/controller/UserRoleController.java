package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
import com.multicar.repository.demo.model.UserRole;
import com.multicar.repository.demo.service.UserRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping
    public ResponseEntity<UserRole> createUserRole(@RequestBody UserRole userRole) {
        UserRole createdUserRole = userRoleService.createUserRole(userRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserRole);
    }

    @GetMapping("/{userRoleId}")
    public ResponseEntity<UserRole> getUserRoleById(@PathVariable String userRoleId) {
        UserRole userRole = userRoleService.getUserRoleById(userRoleId)
                .orElseThrow(() -> new ResourceNotFoundException("User role not found with id: " + userRoleId, ErrorCode.USER_ROLE_NOT_FOUND));
        return ResponseEntity.ok(userRole);
    }

    @GetMapping
    public ResponseEntity<List<UserRole>> getAllUserRoles() {
        List<UserRole> userRoles = userRoleService.getAllUserRoles();
        return ResponseEntity.ok(userRoles);
    }

    @PutMapping("/{userRoleId}")
    public ResponseEntity<UserRole> updateUserRole(
            @PathVariable String userRoleId,
            @RequestBody UserRole userRole) {
        UserRole updatedUserRole = userRoleService.updateUserRole(userRoleId, userRole)
                .orElseThrow(() -> new ResourceNotFoundException("User role not found with id: " + userRoleId, ErrorCode.USER_ROLE_NOT_FOUND));
        return ResponseEntity.ok(updatedUserRole);
    }

    @DeleteMapping("/{userRoleId}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable String userRoleId) {
        boolean deleted = userRoleService.deleteUserRole(userRoleId);
        if (!deleted) {
            throw new ResourceNotFoundException("User role not found with id: " + userRoleId, ErrorCode.USER_ROLE_NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRole>> getUserRolesByUserId(@PathVariable String userId) {
        List<UserRole> userRoles = userRoleService.getUserRolesByUserId(userId);
        return ResponseEntity.ok(userRoles);
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<UserRole>> getUserRolesByRoleId(@PathVariable String roleId) {
        List<UserRole> userRoles = userRoleService.getUserRolesByRoleId(roleId);
        return ResponseEntity.ok(userRoles);
    }

    @GetMapping("/exists/user/{userId}/role/{roleId}")
    public ResponseEntity<Boolean> checkUserRoleExists(
            @PathVariable String userId,
            @PathVariable String roleId) {
        boolean exists = userRoleService.existsByUserIdAndRoleId(userId, roleId);
        return ResponseEntity.ok(exists);
    }
}



