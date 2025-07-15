package com.example.ai_vms.service;

//MaintenanceRecordService

import com.example.ai_vms.model.MaintenanceRecord;
import com.example.ai_vms.model.Vehicle;
import com.example.ai_vms.repository.MaintenanceRecordRepository;
import com.example.ai_vms.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException; //Important fix done
import java.util.List;
// import java.util.Optional;

@Service
public class MaintenanceRecordService {

    @Autowired
    private MaintenanceRecordRepository maintenanceRecordRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    // create maintenance record
    public MaintenanceRecord addMaintenanceRecord(MaintenanceRecord record) {
        if (record.getVehicle() == null || record.getVehicle().getId() == null) {
            throw new EntityNotFoundException("VehicleID must not be null.");
        }

        // Ensure the vehicle exists before adding a maintenance record
        Vehicle vehicle = vehicleRepository.findById(record.getVehicle().getId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with ID: " + record.getVehicle().getId()));
        record.setVehicle(vehicle);  // Ensure correct vehicle association
        return maintenanceRecordRepository.save(record);
    }

    public List<MaintenanceRecord> getAllMaintenanceRecords() {
        return maintenanceRecordRepository.findAll();
    }

    public List<MaintenanceRecord> getMaintenanceRecordsByVehicleId(Long vehicleId) {
        return maintenanceRecordRepository.findByVehicleId(vehicleId);
    }

    public MaintenanceRecord updateMaintenanceRecord(Long id, MaintenanceRecord updatedRecord) {
        MaintenanceRecord existingRecord = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maintenance Record not found with ID: " + id));

        // Update fields
        existingRecord.setDescription(updatedRecord.getDescription());
        existingRecord.setCost(updatedRecord.getCost());
        existingRecord.setMaintenanceDate(updatedRecord.getMaintenanceDate());
        existingRecord.setMaintenanceType(updatedRecord.getMaintenanceType());
        existingRecord.setMileage(updatedRecord.getMileage());
        existingRecord.setTemperature(updatedRecord.getTemperature());
        existingRecord.setVehicleUsage(updatedRecord.getVehicleUsage());

        return maintenanceRecordRepository.save(existingRecord);
    }

    public void deleteMaintenanceRecord(Long id) {
        MaintenanceRecord record = maintenanceRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Maintenance Record not found with ID: " + id));
        maintenanceRecordRepository.delete(record);
    }
}
