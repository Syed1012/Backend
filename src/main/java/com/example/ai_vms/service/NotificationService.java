package com.example.ai_vms.service;

import com.example.ai_vms.model.Booking;
import com.example.ai_vms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendBookingConfirmationEmail(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("musaibkng853@gmail.com"); // Replace with your sender email

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            // Consider logging the exception for further analysis
        }
    }

    public void sendBookingCancellationNotification(Booking booking) {
        try {
            Optional<String> optionalEmail = Optional
                    .ofNullable(userRepository.findEmailById(booking.getUser().getId()));

            if (optionalEmail.isPresent()) {
                String toEmail = optionalEmail.get();
                String subject = "Booking Cancellation";
                String body = String.format(
                        "Dear User,\n\nYour booking with ID %d has been successfully cancelled.\n\nBest regards,\nAI Vehicle Management System",
                        booking.getId());

                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(toEmail);
                message.setSubject(subject);
                message.setText(body);
                message.setFrom("musaibkng853@gmail.com"); // Replace with your sender email

                mailSender.send(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Consider logging the exception for further analysis
        }
    }
}
