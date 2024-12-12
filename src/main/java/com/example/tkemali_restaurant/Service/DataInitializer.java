package com.example.tkemali_restaurant.Service;

import com.example.tkemali_restaurant.models.User;
import com.example.tkemali_restaurant.models.UserRole;
import com.example.tkemali_restaurant.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Проверяем, есть ли администратор
        String adminEmail = "admin@example.com";
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User adminUser = new User();
            adminUser.setEmail(adminEmail);
            adminUser.setPassword(passwordEncoder.encode("1")); // Пароль по умолчанию

            adminUser.setRole(UserRole.ADMIN);

            userRepository.save(adminUser);
            System.out.println("Создан администраторский аккаунт: " + adminEmail);
        }
    }
}
