package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.enums.UserRole;
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
        return roleService.getRoleById(roleId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
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
        return roleService.updateRole(roleId, role)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable String roleId) {
        boolean deleted = roleService.deleteRole(roleId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/name/{roleName}")
    public ResponseEntity<Role> getRoleByName(@PathVariable UserRole roleName) {
        return roleService.getRoleByName(roleName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/exists/name/{roleName}")
    public ResponseEntity<Boolean> checkRoleNameExists(@PathVariable UserRole roleName) {
        boolean exists = roleService.existsByRoleName(roleName);
        return ResponseEntity.ok(exists);
    }
}



