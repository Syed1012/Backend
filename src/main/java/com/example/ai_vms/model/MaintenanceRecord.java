package com.example.ai_vms.model;

//MaintenanceRecord

import jakarta.persistence.*;
import java.time.LocalDate;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "maintenance_records")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "cost", nullable = false)
    private double cost;

    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;

    // Change fetch type to EAGER temporarily for debugging
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "maintenance_type", nullable = false)
    private String maintenanceType;

    @Column(name = "mileage", nullable = false)
    private double mileage;

    @Column(name = "temperature", nullable = false)
    private double temperature;

    @Column(name = "vehicle_usage", nullable = false)
    private String vehicleUsage; // daily or weekly

    // Default constructor (required by JPA)
    public MaintenanceRecord() {
    }

    public MaintenanceRecord(String description, double cost, LocalDate maintenanceDate, Vehicle vehicle,
            String maintenanceType, double mileage, double temperature, String vehicleUsage) {
        this.description = description;
        this.cost = cost;
        this.maintenanceDate = maintenanceDate;
        this.vehicle = vehicle;
        this.maintenanceType = maintenanceType;
        this.mileage = mileage;
        this.temperature = temperature;
        this.vehicleUsage = vehicleUsage;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getMaintenanceDate() {
        return maintenanceDate;
    }

    public void setMaintenanceDate(LocalDate maintenanceDate) {
        this.maintenanceDate = maintenanceDate;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getMaintenanceType() {
        return maintenanceType;
    }

    public void setMaintenanceType(String maintenanceType) {
        this.maintenanceType = maintenanceType;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getVehicleUsage() {
        return vehicleUsage;
    }

    public void setVehicleUsage(String vehicleUsage) {
        this.vehicleUsage = vehicleUsage;
    }

}
