package com.example.tkemali_restaurant.Repository;

import com.example.tkemali_restaurant.models.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {
}