package com.example.tkemali_restaurant.Repository;

import com.example.tkemali_restaurant.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    User findByFullName(String username);
}
