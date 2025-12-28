package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.enums.UserRole;
import com.multicar.repository.demo.exception.ErrorCode;
import com.multicar.repository.demo.exception.ResourceNotFoundException;
import com.multicar.repository.demo.model.Role;
import com.multicar.repository.demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<Role> getRoleById(@PathVariable String roleId) {
        Role role = roleService.getRoleById(roleId)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId, ErrorCode.ROLE_NOT_FOUND));
        return ResponseEntity.ok(role);
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Role> updateRole(
            @PathVariable String roleId,
            @RequestBody Role role) {
        Role updatedRole = roleService.updateRole(roleId, role)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + roleId, ErrorCode.ROLE_NOT_FOUND));
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable String roleId) {
        boolean deleted = roleService.deleteRole(roleId);
        if (!deleted) {
            throw new ResourceNotFoundException("Role not found with id: " + roleId, ErrorCode.ROLE_NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/name/{roleName}")
    public ResponseEntity<Role> getRoleByName(@PathVariable UserRole roleName) {
        Role role = roleService.getRoleByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with name: " + roleName, ErrorCode.ROLE_NOT_FOUND));
        return ResponseEntity.ok(role);
    }

    @GetMapping("/exists/name/{roleName}")
    public ResponseEntity<Boolean> checkRoleNameExists(@PathVariable UserRole roleName) {
        boolean exists = roleService.existsByRoleName(roleName);
        return ResponseEntity.ok(exists);
    }
}



