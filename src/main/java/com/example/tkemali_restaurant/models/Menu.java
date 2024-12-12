package com.example.tkemali_restaurant.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private double price;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "image_version")
    private int imageVersion;

    // Новое поле для хранения пути к изображению
    @Column(name = "image_path") // Указываем имя столбца в базе данных
    private String imagePath; // Путь к изображению


}