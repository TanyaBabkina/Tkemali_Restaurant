package com.example.tkemali_restaurant.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class UserRegistrationDto {
    @NotBlank(message = "Email обязателен")
    @Email(message = "Неверный формат email")
    private String email;
    
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;
    
    @NotBlank(message = "Подтверждение пароля обязательно")
    private String passwordConfirm;
    
    @NotBlank(message = "Имя обязательно")
    private String fullName;
    
    private String phoneNumber;
}