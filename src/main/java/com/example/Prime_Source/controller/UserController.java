package com.example.Prime_Source.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Prime_Source.entity.User;
import com.example.Prime_Source.enums.RoleStatus;
import com.example.Prime_Source.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class LoginResponse {
        public String message;
        public String role;
    }
    // ✅ 1. Check if Admin exists
    @GetMapping("/admin-exists")
    public ResponseEntity<Boolean> adminExists() {
        boolean exists = userService.isAdminPresent();
        return ResponseEntity.ok(exists);
    }

    // ✅ 2. Register first admin (only one allowed)
    @PostMapping("/create-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody User user) {
        if (userService.isAdminPresent()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Admin already exists");
        }
        user.setRole(RoleStatus.ADMIN);
        userService.createAdmin(user);
        return ResponseEntity.ok("Admin registered successfully");
    }

    // ✅ 3. Admin creates recruiter (many allowed)
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create-recruiter")
    public ResponseEntity<?> createRecruiter(@RequestBody User user) {
        try {
            user.setRole(RoleStatus.RECRUITER);
            userService.createRecruiter(user);
            return ResponseEntity.ok("Recruiter created successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // ✅ 4. Get all users
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // ✅ 5. Get user by ID
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 6. Get user by username
//    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 7. Update user (admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User updatedUser = userService.updateUser(id, user);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ✅ 8. Delete user (admin only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean deleted = userService.deleteUser(id);
        if (deleted) {
            return ResponseEntity.ok("User with ID " + id + " deleted.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " not found.");
        }
    }
    //login controller

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // For demonstration, just hardcoded admin login check:
        if ("admin".equals(loginRequest.username) && "adminpass".equals(loginRequest.password)) {
            LoginResponse resp = new LoginResponse();
            resp.message = "Login successful";
            resp.role = "ADMIN";
            return ResponseEntity.ok(resp);
        }
        // Could add more logic to verify against DB, etc.

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
    }
}  