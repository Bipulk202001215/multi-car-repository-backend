package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
import com.multicar.repository.demo.model.RolePermission;
import com.multicar.repository.demo.model.RolePermissionsResponse;
import com.multicar.repository.demo.service.RolePermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @PostMapping
    public ResponseEntity<RolePermission> createRolePermission(@RequestBody RolePermission rolePermission) {
        RolePermission createdRolePermission = rolePermissionService.createRolePermission(rolePermission);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRolePermission);
    }

    @GetMapping("/{rolePermissionId}")
    public ResponseEntity<RolePermission> getRolePermissionById(@PathVariable String rolePermissionId) {
        RolePermission rolePermission = rolePermissionService.getRolePermissionById(rolePermissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Role permission not found with id: " + rolePermissionId, ErrorCode.ROLE_PERMISSION_NOT_FOUND));
        return ResponseEntity.ok(rolePermission);
    }

    @GetMapping
    public ResponseEntity<List<RolePermission>> getAllRolePermissions() {
        List<RolePermission> rolePermissions = rolePermissionService.getAllRolePermissions();
        return ResponseEntity.ok(rolePermissions);
    }

    @PutMapping("/{rolePermissionId}")
    public ResponseEntity<RolePermission> updateRolePermission(
            @PathVariable String rolePermissionId,
            @RequestBody RolePermission rolePermission) {
        RolePermission updatedRolePermission = rolePermissionService.updateRolePermission(rolePermissionId, rolePermission)
                .orElseThrow(() -> new ResourceNotFoundException("Role permission not found with id: " + rolePermissionId, ErrorCode.ROLE_PERMISSION_NOT_FOUND));
        return ResponseEntity.ok(updatedRolePermission);
    }

    @DeleteMapping("/{rolePermissionId}")
    public ResponseEntity<Void> deleteRolePermission(@PathVariable String rolePermissionId) {
        boolean deleted = rolePermissionService.deleteRolePermission(rolePermissionId);
        if (!deleted) {
            throw new ResourceNotFoundException("Role permission not found with id: " + rolePermissionId, ErrorCode.ROLE_PERMISSION_NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/role/{roleId}")
    public ResponseEntity<RolePermissionsResponse> getRolePermissionsByRoleId(@PathVariable String roleId) {
        RolePermissionsResponse response = rolePermissionService.getRolePermissionsResponseByRoleId(roleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/permission/{permissionId}")
    public ResponseEntity<List<RolePermission>> getRolePermissionsByPermissionId(@PathVariable String permissionId) {
        List<RolePermission> rolePermissions = rolePermissionService.getRolePermissionsByPermissionId(permissionId);
        return ResponseEntity.ok(rolePermissions);
    }

    @GetMapping("/exists/role/{roleId}/permission/{permissionId}")
    public ResponseEntity<Boolean> checkRolePermissionExists(
            @PathVariable String roleId,
            @PathVariable String permissionId) {
        boolean exists = rolePermissionService.existsByRoleIdAndPermissionId(roleId, permissionId);
        return ResponseEntity.ok(exists);
    }
}



