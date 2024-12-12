package com.example.tkemali_restaurant.mapper;

import com.example.tkemali_restaurant.models.User;
import com.example.tkemali_restaurant.dto.UserDto;
import com.example.tkemali_restaurant.dto.UserRegistrationDto;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    public User toUser(UserRegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setFullName(dto.getFullName());
        user.setPhoneNumber(dto.getPhoneNumber());
        return user;
    }

    public UserDto toUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFullName(user.getFullName());
        dto.setPhoneNumber(user.getPhoneNumber());
        return dto;
    }


}