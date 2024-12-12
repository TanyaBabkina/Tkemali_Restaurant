package com.example.tkemali_restaurant.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    private String fullName;
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

}
