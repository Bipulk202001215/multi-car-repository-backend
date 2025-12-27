package com.multicar.repository.demo.controller;

import com.multicar.repository.demo.enums.UserType;
import com.multicar.repository.demo.model.User;
import com.multicar.repository.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable String userId) {
        return userService.getUserById(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable String userId,
            @RequestBody User user) {
        return userService.updateUser(userId, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        boolean deleted = userService.deleteUser(userId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/email/{emailId}")
    public ResponseEntity<User> getUserByEmailId(@PathVariable String emailId) {
        return userService.getUserByEmailId(emailId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/company/{companyId}")
    public ResponseEntity<List<User>> getUsersByCompanyId(@PathVariable String companyId) {
        List<User> users = userService.getUsersByCompanyId(companyId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/type/{userType}")
    public ResponseEntity<List<User>> getUsersByUserType(@PathVariable UserType userType) {
        List<User> users = userService.getUsersByUserType(userType);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/exists/email/{emailId}")
    public ResponseEntity<Boolean> checkEmailExists(@PathVariable String emailId) {
        boolean exists = userService.existsByEmailId(emailId);
        return ResponseEntity.ok(exists);
    }
}



