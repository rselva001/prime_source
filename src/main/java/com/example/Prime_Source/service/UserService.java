package com.example.Prime_Source.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.Prime_Source.entity.User;
import com.example.Prime_Source.enums.RoleStatus;
import com.example.Prime_Source.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Creates the first admin user.
     * Only one admin is allowed in the system.
     * Password is encoded before saving.
     *
     * @param user User object containing admin details
     * @return saved User object
     * @throws RuntimeException if an admin already exists
     */
    public User createAdmin(User user) {
        boolean adminExists = userRepository.existsByRole(RoleStatus.ADMIN);
        if (adminExists) {
            throw new RuntimeException("Admin user already exists. Only one admin allowed.");
        }
        user.setRole(RoleStatus.ADMIN);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password here
        return userRepository.save(user);
    }

    /**
     * Creates a recruiter user.
     * Ensures username is unique.
     * Password is encoded before saving.
     *
     * @param user User object containing recruiter details
     * @return saved User object
     * @throws RuntimeException if username already exists
     */
    public User createRecruiter(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username '" + user.getUsername() + "' already exists.");
        }
        user.setRole(RoleStatus.RECRUITER);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Also encode password here!
        return userRepository.save(user);
    }

    /**
     * Retrieves a list of all users.
     *
     * @return List of User objects
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Finds a user by their ID.
     *
     * @param id User ID
     * @return Optional containing User if found, else empty
     */
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their username.
     *
     * @param username Username string
     * @return Optional containing User if found, else empty
     */
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Updates a user by ID.
     * - Checks admin role constraint: only one admin allowed.
     * - Updates username.
     * - Encodes password if it's provided.
     * - Updates role.
     *
     * @param id          ID of the user to update
     * @param updatedUser User object with updated details
     * @return updated User object
     * @throws RuntimeException if user not found or admin constraint violated
     */
    public User updateUser(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        // Prevent multiple admins
        if (updatedUser.getRole() == RoleStatus.ADMIN) {
            boolean adminExists = userRepository.existsByRole(RoleStatus.ADMIN);
            if (adminExists && !user.getRole().equals(RoleStatus.ADMIN)) {
                throw new RuntimeException("Admin user already exists. Only one admin allowed.");
            }
        }

        user.setUsername(updatedUser.getUsername());

        // Encode password if provided and not empty
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        user.setRole(updatedUser.getRole());

        return userRepository.save(user);
    }

    /**
     * Deletes a user by ID.
     *
     * @param id User ID
     * @return true if deleted successfully, false if user not found
     */
    public boolean deleteUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Checks if an admin user already exists.
     *
     * @return true if admin exists, false otherwise
     */
    public boolean isAdminPresent() {
        return userRepository.existsByRole(RoleStatus.ADMIN);
    }
}
