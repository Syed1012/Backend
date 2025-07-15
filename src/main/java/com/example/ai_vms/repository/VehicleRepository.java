package com.example.ai_vms.repository;



import com.example.ai_vms.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    List<Vehicle> findByAvailable(boolean available);

    List<Vehicle> findByUserId(Long userId);
}

//created a repository interface to handle database operations for the Vehicle entity.