package com.example.ai_vms.controller;

import com.example.ai_vms.exception.ResourceNotFoundException;
import com.example.ai_vms.model.User;
import com.example.ai_vms.model.Vehicle;
import com.example.ai_vms.repository.UserRepository;
// import com.example.ai_vms.repository.VehicleRepository;
import com.example.ai_vms.service.VehicleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VehicleService vehicleService;

    @GetMapping
    public ResponseEntity<List<Vehicle>> getAllVehicles() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicles);
    }

    // New endpoint to get vehicles by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Vehicle>> getVehiclesByUserId(@PathVariable Long userId) {
        
        List<Vehicle> vehicles = vehicleService.getVehiclesByUserId(userId);
        return ResponseEntity.ok(vehicles);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<Vehicle> createVehicleForUser(@PathVariable Long userId, @RequestBody Vehicle vehicle) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        vehicle.setUser(user);
        Vehicle savedVehicle = vehicleService.addVehicle(vehicle);
        return new ResponseEntity<>(savedVehicle, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vehicle> getVehicleById(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        return ResponseEntity.ok(vehicle);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable Long id, @RequestBody Vehicle vehicleDetails) {
        Vehicle updatedVehicle = vehicleService.updateVehicle(id, vehicleDetails);
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/available")
    public ResponseEntity<List<Vehicle>> getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleService.getAvailableVehicles();
        return ResponseEntity.ok(vehicles);
    }

    @GetMapping("/average-fuel-efficiency")
    public ResponseEntity<Double> getAverageFuelEfficiency() {
        List<Vehicle> vehicles = vehicleService.getAllVehicles();
        double averageEfficiency = vehicles.stream().mapToDouble(Vehicle::getFuelEfficiency).average().orElse(0);
        return ResponseEntity.ok(averageEfficiency);
    }

    @GetMapping("/{id}/usage-report")
    public ResponseEntity<String> generateVehicleUsageReport(@PathVariable Long id) {
        Vehicle vehicle = vehicleService.getVehicleById(id);
        String report = "Vehicle " + vehicle.getMake() + " " + vehicle.getModel() + " (" + vehicle.getYear() + ") " +
                "with registration number " + vehicle.getRegistrationNumber() + " has driven " +
                vehicle.getCurrentMileage() + " miles. " +
                "It is currently " + (vehicle.isAvailable() ? "available" : "not available") + " for use.";
        return ResponseEntity.ok(report);
    }
}