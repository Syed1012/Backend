package com.example.ai_vms.repository;

import com.example.ai_vms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    // Custom query method to fetch the email by user ID
    String findEmailById(Long id);

    // Add this method to check if the email already exists
    boolean existsByEmail(String email);
}
