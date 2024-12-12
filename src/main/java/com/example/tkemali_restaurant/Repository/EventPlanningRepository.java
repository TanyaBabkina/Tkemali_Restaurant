package com.example.tkemali_restaurant.Repository;

import com.example.tkemali_restaurant.models.EventPlanning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventPlanningRepository extends JpaRepository<EventPlanning, Long> {
}