package com.example.ai_vms.service;

import com.example.ai_vms.model.Vehicle;
import com.example.ai_vms.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getAvailableVehicles() {
        return vehicleRepository.findByAvailable(true);
    }

    public Vehicle getVehicleById(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.orElseThrow(() -> new RuntimeException("Vehicle not found with ID: " + id));
    }

    // New method to get vehicles by user ID
    public List<Vehicle> getVehiclesByUserId(Long userId) {
        return vehicleRepository.findByUserId(userId);
    }

    public Vehicle addVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    public Vehicle updateVehicle(Long id, Vehicle vehicleDetails) {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setMake(vehicleDetails.getMake());
        vehicle.setModel(vehicleDetails.getModel());
        vehicle.setYear(vehicleDetails.getYear());
        vehicle.setVin(vehicleDetails.getVin());
        vehicle.setRegistrationNumber(vehicleDetails.getRegistrationNumber());
        vehicle.setVehicleType(vehicleDetails.getVehicleType());
        vehicle.setCurrentMileage(vehicleDetails.getCurrentMileage());
        vehicle.setFuelEfficiency(vehicleDetails.getFuelEfficiency());
        vehicle.setLastMaintenanceDate(vehicleDetails.getLastMaintenanceDate());
        vehicle.setAvailable(vehicleDetails.isAvailable());
        vehicle.setAvailableFrom(vehicleDetails.getAvailableFrom());
        vehicle.setAvailableTill(vehicleDetails.getAvailableTill());
        vehicle.setPickupLocation(vehicleDetails.getPickupLocation());
        vehicle.setDropoffLocation(vehicleDetails.getDropoffLocation());
        return vehicleRepository.save(vehicle);
    }

    public void deleteVehicle(Long id) {
        Vehicle vehicle = getVehicleById(id);
        vehicleRepository.delete(vehicle);
    }
}