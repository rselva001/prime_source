package com.example.Prime_Source.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Prime_Source.entity.User;
import com.example.Prime_Source.enums.RoleStatus;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByRole(RoleStatus role);  // Check if any user has the given role
}
