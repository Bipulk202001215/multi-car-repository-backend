package com.multicar.repository.demo.controller;

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
        return userRoleService.getUserRoleById(userRoleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
        return userRoleService.updateUserRole(userRoleId, userRole)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userRoleId}")
    public ResponseEntity<Void> deleteUserRole(@PathVariable String userRoleId) {
        boolean deleted = userRoleService.deleteUserRole(userRoleId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
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



