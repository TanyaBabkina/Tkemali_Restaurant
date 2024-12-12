package com.example.tkemali_restaurant.Service;

import com.example.tkemali_restaurant.models.Category;
import com.example.tkemali_restaurant.Repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category getCategoryById(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        return categoryOptional.orElse(null);
    }
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}
