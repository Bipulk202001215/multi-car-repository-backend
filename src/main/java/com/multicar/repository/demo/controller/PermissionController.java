package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
import com.multicar.repository.demo.model.Permission;
import com.multicar.repository.demo.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    public ResponseEntity<Permission> createPermission(@RequestBody Permission permission) {
        Permission createdPermission = permissionService.createPermission(permission);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    @GetMapping("/{permissionId}")
    public ResponseEntity<Permission> getPermissionById(@PathVariable String permissionId) {
        Permission permission = permissionService.getPermissionById(permissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + permissionId, ErrorCode.PERMISSION_NOT_FOUND));
        return ResponseEntity.ok(permission);
    }

    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        List<Permission> permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(permissions);
    }

    @PutMapping("/{permissionId}")
    public ResponseEntity<Permission> updatePermission(
            @PathVariable String permissionId,
            @RequestBody Permission permission) {
        Permission updatedPermission = permissionService.updatePermission(permissionId, permission)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with id: " + permissionId, ErrorCode.PERMISSION_NOT_FOUND));
        return ResponseEntity.ok(updatedPermission);
    }

    @DeleteMapping("/{permissionId}")
    public ResponseEntity<Void> deletePermission(@PathVariable String permissionId) {
        boolean deleted = permissionService.deletePermission(permissionId);
        if (!deleted) {
            throw new ResourceNotFoundException("Permission not found with id: " + permissionId, ErrorCode.PERMISSION_NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name/{permissionName}")
    public ResponseEntity<Permission> getPermissionByName(@PathVariable com.multicar.repository.demo.enums.Permission permissionName) {
        Permission permission = permissionService.getPermissionByName(permissionName)
                .orElseThrow(() -> new ResourceNotFoundException("Permission not found with name: " + permissionName, ErrorCode.PERMISSION_NOT_FOUND));
        return ResponseEntity.ok(permission);
    }

    @GetMapping("/exists/name/{permissionName}")
    public ResponseEntity<Boolean> checkPermissionNameExists(@PathVariable com.multicar.repository.demo.enums.Permission permissionName) {
        boolean exists = permissionService.existsByPermissionName(permissionName);
        return ResponseEntity.ok(exists);
    }
}

