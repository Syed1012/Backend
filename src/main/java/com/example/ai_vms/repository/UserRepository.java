package com.example.ai_vms.repository;

import com.example.ai_vms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Fetch user by email
    Optional<User> findByEmail(String email);

    // Fetch email of user by ID - fixed with @Query
    @Query("SELECT u.email FROM User u WHERE u.id = :id")
    String findEmailById(@Param("id") Long id);

    // Check if email already exists
    boolean existsByEmail(String email);
}