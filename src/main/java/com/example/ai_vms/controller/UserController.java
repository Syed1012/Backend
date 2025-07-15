//UserController.java
package com.example.ai_vms.controller;

import com.example.ai_vms.model.User;
import com.example.ai_vms.repository.UserRepository;
import com.example.ai_vms.service.UserService;
import com.example.ai_vms.util.JwtUtil;
import com.example.ai_vms.exception.ResourceNotFoundException;
import com.example.ai_vms.dto.ForgotPasswordRequest;
import com.example.ai_vms.dto.ResetPasswordRequest;
import com.example.ai_vms.exception.InvalidCredentialsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    // Register user
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody User user) {

        // Check for existing user by email
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use.");
        }

        User registeredUser = userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully with ID: " + registeredUser.getId());
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User user) {
        Optional<User> existingUser = userService.findByEmail(user.getEmail());

        if (existingUser.isEmpty()) {
            throw new ResourceNotFoundException("User with email " + user.getEmail() + " not found.");
        }

        if (!userService.checkPassword(user.getPassword(), existingUser.get().getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials provided.");
        }

        // Get user's role
        Integer userRole = existingUser.get().getRole();
        Long userId = existingUser.get().getId(); // This to fetch the user ID

        // Generate JWT token with username, userId, and role
        String token = jwtUtil.generateToken(existingUser.get().getEmail(), userId, userRole);

        // Return the token, role, and userId in the response
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("role", userRole); // Include role in the response
        response.put("userId", userId); // Include user ID in the response
        response.put("message", "Login successful");
        response.put("success", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        String email = request.getEmail();
        userService.initiatePasswordReset(email);

        // Create a structured response
        Map<String, Object> response = new HashMap<>();
        response.put("message", "OTP sent to your email!");
        response.put("success", true); // Add a success flag

        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestBody ResetPasswordRequest request) {
        Map<String, Object> response = new HashMap<>();

        boolean success = userService.resetPassword(request.getEmail(), request.getOtp(), request.getNewPassword());

        if (success) {
            response.put("message", "Password reset successful!");
            response.put("success", true);
            return ResponseEntity.ok(response); // HTTP 200 OK
        } else {
            response.put("message", "Invalid OTP!");
            response.put("success", false);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // HTTP 400 BAD REQUEST
        }
    }

    // Fetch user by email (additional endpoint for demonstration)
    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found."));
        return ResponseEntity.ok(user);
    }

    // New Profile Endpoint
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        // Extract the token part after 'Bearer '
        String jwtToken = token.substring(7);

        // Extract email from the token
        String userEmail = jwtUtil.extractUsername(jwtToken);

        // Fetch user by email
        Optional<User> userOptional = userService.findByEmail(userEmail);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User profile not found.");
        }

        // Return the user's profile details
        return ResponseEntity.ok(userOptional.get());
    }
}
