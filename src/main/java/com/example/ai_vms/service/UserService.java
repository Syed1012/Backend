package com.example.ai_vms.service;

import com.example.ai_vms.exception.ResourceNotFoundException;
import com.example.ai_vms.model.User;
import com.example.ai_vms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Autowiring the PasswordEncoder interface

    @Autowired
    private JavaMailSender mailSender;

    // User register process
    @Transactional
    public User registerUser(User user) {
        // Check if the user already exists by email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        // Hash the password before saving it
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save the user in the database
        return userRepository.save(user);
    }

    // Finding User process
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Password check process
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    // Simulating OTP cache storage
    private Map<String, String> otpCache = new HashMap<>();

    // Generate and store OTP in the cache (This could be in Redis or any persistent
    // cache in production)
    public void initiatePasswordReset(String email) {
        Optional<User> user = userRepository.findByEmail(email);

        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User with email " + email + " not found.");
        }

        // Generate OTP
        String otp = generateOTP();
        otpCache.put(email, otp);

        // Send the OTP via email
        sendEmailWithOTP(email, otp); // Call the method to send email

        System.out.println("OTP for " + email + ": " + otp); // Optional
    }

    private void sendEmailWithOTP(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Your OTP code is: " + otp);
        mailSender.send(message); // Sending the email
    }

    // Method to generate a 6-digit OTP
    private String generateOTP() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }

    // Validate OTP and reset password
    public boolean resetPassword(String email, String otp, String newPassword) {
        // Validate email exists
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new ResourceNotFoundException("User with email " + email + " not found.");
        }

        // Check if OTP is valid
        String cachedOTP = otpCache.get(email);
        if (cachedOTP == null || !cachedOTP.equals(otp)) {
            return false; // Invalid OTP
        }

        System.out.println(cachedOTP);

        // If OTP is valid, reset the password
        User user = userOptional.get();
        String encodedPassword = passwordEncoder.encode(newPassword); // Hash new password
        user.setPassword(encodedPassword);

        // Save updated user in the database
        userRepository.save(user);

        // Remove the OTP from cache after successful reset
        otpCache.remove(email);

        return true; // Password reset successful
    }

}
