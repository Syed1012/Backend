package com.example.ai_vms.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private int year;
    private String vin;
    private String registrationNumber;
    private String vehicleType;

    private double currentMileage;
    private double fuelEfficiency;

    @Temporal(TemporalType.DATE)
    private Date lastMaintenanceDate;

    private boolean available;

    @Temporal(TemporalType.DATE)
    private Date availableFrom;

    @Temporal(TemporalType.DATE)
    private Date availableTill;

    private String pickupLocation;
    private String dropoffLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore // Prevents circular reference
    private User user;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore // Prevents circular reference
    private List<MaintenanceRecord> maintenanceRecords;

    public Vehicle() {
    }

    public Vehicle(String make, String model, int year, String vin, String registrationNumber, String vehicleType,
                   double currentMileage, double fuelEfficiency, Date lastMaintenanceDate, boolean available,
                   Date availableFrom, Date availableTill, String pickupLocation, String dropoffLocation, User user) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.vin = vin;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.currentMileage = currentMileage;
        this.fuelEfficiency = fuelEfficiency;
        this.lastMaintenanceDate = lastMaintenanceDate;
        this.available = available;
        this.availableFrom = availableFrom;
        this.availableTill = availableTill;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.user = user;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getCurrentMileage() {
        return currentMileage;
    }

    public void setCurrentMileage(double currentMileage) {
        this.currentMileage = currentMileage;
    }

    public double getFuelEfficiency() {
        return fuelEfficiency;
    }

    public void setFuelEfficiency(double fuelEfficiency) {
        this.fuelEfficiency = fuelEfficiency;
    }

    public Date getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }

    public void setLastMaintenanceDate(Date lastMaintenanceDate) {
        this.lastMaintenanceDate = lastMaintenanceDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Date getAvailableFrom() {
        return availableFrom;
    }

    public void setAvailableFrom(Date availableFrom) {
        this.availableFrom = availableFrom;
    }

    public Date getAvailableTill() {
        return availableTill;
    }

    public void setAvailableTill(Date availableTill) {
        this.availableTill = availableTill;
    }

    public String getPickupLocation() {
        return pickupLocation;
    }

    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }

    public String getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(String dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<MaintenanceRecord> getMaintenanceRecords() {
        return maintenanceRecords;
    }

    public void setMaintenanceRecords(List<MaintenanceRecord> maintenanceRecords) {
        this.maintenanceRecords = maintenanceRecords;
    }
}
