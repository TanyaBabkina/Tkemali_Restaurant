package com.example.tkemali_restaurant.Repository;

import com.example.tkemali_restaurant.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findById(Long id);
    @Query("SELECT e FROM Event e LEFT JOIN FETCH e.eventPlannings ep LEFT JOIN FETCH ep.menu WHERE e.id = :id")
    Optional<Event> findByIdWithDetails(@Param("id") Long id);
}
