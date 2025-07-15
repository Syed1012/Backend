package com.example.ai_vms.controller;

//MaintenanceRecordController

import com.example.ai_vms.model.MaintenanceRecord;
// import com.example.ai_vms.model.Vehicle;
import com.example.ai_vms.service.MaintenanceRecordService;
// import com.example.ai_vms.service.VehicleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/maintenance-records")
public class MaintenanceRecordController {

    @Autowired
    private MaintenanceRecordService maintenanceRecordService;

    // @Autowired
    // private VehicleService vehicleService;

    // Add a new maintenance record
    @PostMapping("/add")
    public ResponseEntity<?> addMaintenanceRecord(@RequestBody MaintenanceRecord maintenanceRecord) {
        try {
            MaintenanceRecord createdRecord = maintenanceRecordService.addMaintenanceRecord(maintenanceRecord);
            return ResponseEntity.ok(createdRecord);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred: " + e.getMessage());
        }
    }

    // Get all maintenance records
    @GetMapping("/all")
    public ResponseEntity<List<MaintenanceRecord>> getAllMaintenanceRecords() {
        List<MaintenanceRecord> records = maintenanceRecordService.getAllMaintenanceRecords();
        return ResponseEntity.ok(records);
    }

    // Get maintenance records by vehicle ID
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<MaintenanceRecord>> getMaintenanceRecordsByVehicle(@PathVariable Long vehicleId) {
        List<MaintenanceRecord> records = maintenanceRecordService.getMaintenanceRecordsByVehicleId(vehicleId);
        return ResponseEntity.ok(records);
    }

    // Update a specific maintenance record
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateMaintenanceRecord(@PathVariable Long id,
            @RequestBody MaintenanceRecord maintenanceRecord) {
        try {
            MaintenanceRecord updatedRecord = maintenanceRecordService.updateMaintenanceRecord(id, maintenanceRecord);
            return ResponseEntity.ok(updatedRecord);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Delete a specific maintenance record
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteMaintenanceRecord(@PathVariable Long id) {
        try {
            maintenanceRecordService.deleteMaintenanceRecord(id);
            return ResponseEntity.ok("Maintenance record deleted successfully.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
