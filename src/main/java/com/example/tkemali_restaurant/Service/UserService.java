package com.example.tkemali_restaurant.Service;


import com.example.tkemali_restaurant.exception.EmailAlreadyExistsException;
import com.example.tkemali_restaurant.models.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tkemali_restaurant.models.User;
import com.example.tkemali_restaurant.Repository.UserRepository;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }


@Transactional
public User registerUser(User user) {
    if (userRepository.existsByEmail(user.getEmail())) {
        throw new EmailAlreadyExistsException("Email уже используется");
    }

    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
}


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
    }
//    @Transactional
//    public User updateUserProfile(String email, String fullName, String phoneNumber) {
//        User user = getUserByEmail(email);
//        user.setFullName(fullName);
//        user.setPhoneNumber(phoneNumber);
//        return userRepository.save(user);
//    }

    @Transactional(readOnly = true)
    public boolean verifyPassword(User user, String password) {
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        User user = getUserByEmail(email);
        
        if (!verifyPassword(user, oldPassword)) {
            throw new RuntimeException("Неверный текущий пароль");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
    @Transactional
    public void updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        userRepository.save(existingUser);
    }

    @Transactional
    public void updatePassword(Long id, String newPassword) {
        User existingUser = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
    }
    @Transactional
    public void updateUserByEmail(String email, User user) {
        System.out.println("EMAIL in Servise" + email + " ");
        System.out.println("EMAIL in Servise" + user.getEmail() + " ");
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        userRepository.save(existingUser);
    }

    @Transactional
    public void updatePasswordByEmail(String email, String newPassword) {
        User existingUser = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("User not found"));
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(existingUser);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void updateUserRole(Long id, UserRole role) {
        User user = userRepository.findById(id).orElseThrow();
        user.setRole(role);
        userRepository.save(user);
    }
//    public void addUser(User user) {
//        userRepository.save(user);
//    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}