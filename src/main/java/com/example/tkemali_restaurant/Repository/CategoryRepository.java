package com.example.tkemali_restaurant.Repository;

import com.example.tkemali_restaurant.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAll();
}