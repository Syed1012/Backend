package com.example.ai_vms.service;

import com.example.ai_vms.model.Booking;
import com.example.ai_vms.repository.BookingRepository;
import com.example.ai_vms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class ReminderService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserRepository userRepository; // Inject UserRepository to fetch user email

    @Scheduled(cron = "0 0 12 * * ?") // Run every day at 12 PM
    public void sendBookingReminders() {
        Date currentDate = new Date();
        List<Booking> upcomingBookings = bookingRepository.findAll();

        for (Booking booking : upcomingBookings) {
            long differenceInMillis = booking.getStartDate().getTime() - currentDate.getTime();
            long differenceInHours = TimeUnit.MILLISECONDS.toHours(differenceInMillis);

            // Check if the booking starts in 24 hours
            if (differenceInHours <= 24 && differenceInHours > 0) {
                // Fetch the user's email using userId
                Optional<String> optionalEmail = Optional.ofNullable(userRepository.findEmailById(booking.getUser().getId()));
                
                if (optionalEmail.isPresent()) {
                    String toEmail = optionalEmail.get();
                    String subject = "Booking Reminder";
                    String body = "Dear User,\n\nThis is a reminder for your upcoming booking for vehicle " +
                            booking.getVehicle().getModel() + " starting on " + booking.getStartDate() + ".\n\nThank you!";
                    notificationService.sendBookingConfirmationEmail(toEmail, subject, body);
                }
            }
        }
    }
}
