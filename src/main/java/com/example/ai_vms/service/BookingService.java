package com.example.ai_vms.service;

import com.example.ai_vms.exception.ResourceNotFoundException;
import com.example.ai_vms.exception.VehicleNotAvailableException;
import com.example.ai_vms.model.Booking;
import com.example.ai_vms.model.User;
import com.example.ai_vms.model.Vehicle;
import com.example.ai_vms.repository.BookingRepository;
import com.example.ai_vms.repository.UserRepository;
import com.example.ai_vms.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Booking createBooking(Booking booking) {
        User user = userRepository.findById(booking.getUser().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + booking.getUser().getId()));

        Vehicle vehicle = vehicleRepository.findById(booking.getVehicle().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + booking.getVehicle().getId()));

        if (vehicle.isAvailable()) {
            vehicle.setAvailable(false);
            vehicleRepository.save(vehicle);

            booking.setUser(user);
            booking.setBookingDate(new Date());
            Booking savedBooking = bookingRepository.save(booking);

            sendBookingConfirmationNotification(savedBooking);

            return savedBooking;
        } else {
            throw new VehicleNotAvailableException("Vehicle is not available for booking with ID: " + booking.getVehicle().getId());
        }
    }

    public Booking updateBooking(Long id, Booking bookingDetails) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + id));

        booking.setStartDate(bookingDetails.getStartDate());
        booking.setEndDate(bookingDetails.getEndDate());
        booking.setStartTime(bookingDetails.getStartTime());
        booking.setEndTime(bookingDetails.getEndTime());
        booking.setPickupLocation(bookingDetails.getPickupLocation());
        booking.setDropoffLocation(bookingDetails.getDropoffLocation());
        booking.setStatus(bookingDetails.getStatus());

        // add more fields to update based on requirements

        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
    }

    public void deleteBooking(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));

        Vehicle vehicle = booking.getVehicle();
        if (vehicle != null) {
            vehicle.setAvailable(true);
            vehicleRepository.save(vehicle);
        }

        bookingRepository.deleteById(id);
        notificationService.sendBookingCancellationNotification(booking);
    }



    public List<Booking> getBookingsByVehicleId(Long vehicleId) {
        vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleId));

        return bookingRepository.findByVehicleId(vehicleId);
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        return bookingRepository.findByUserId(userId);
    }

    private void sendBookingConfirmationNotification(Booking booking) {
        String toEmail = booking.getUser().getEmail();
        String subject = "Booking Confirmation";
        String body = String.format(
                "Dear %s,\n\nYour booking with ID %d has been successfully created.\nStart Date: %s\nEnd Date: %s\nPickup Location: %s\nDropoff Location: %s\nStatus: %s\n\nBest regards,\nAI Vehicle Management System",
                booking.getUser().getFirstName(), booking.getId(), booking.getStartDate(), booking.getEndDate(),
                booking.getPickupLocation(), booking.getDropoffLocation(), booking.getStatus());

        notificationService.sendBookingConfirmationEmail(toEmail, subject, body);
    }
}