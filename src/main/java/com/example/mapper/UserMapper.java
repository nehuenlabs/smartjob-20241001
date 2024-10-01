package com.example.mapper;

import com.example.dto.UserDTO;
import com.example.domain.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .isActive(user.isActive())
                .created(user.getCreated())
                .modified(user.getModified())
                .lastLogin(user.getLastLogin())
                .build();

        if (user.getPhones() != null) {
            userDTO.setPhones(user.getPhones().stream()
                    .map(PhoneMapper::toDto)
                    .collect(Collectors.toList()));
        }

        return userDTO;
    }

    public static User toEntity(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = User.builder()
                .id(userDTO.getId())
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .isActive(userDTO.isActive())
                .created(userDTO.getCreated())
                .modified(userDTO.getModified())
                .lastLogin(userDTO.getLastLogin())
                .build();

        if (userDTO.getPhones() != null) {
            user.setPhones(userDTO.getPhones().stream()
                    .map(PhoneMapper::toEntity)
                    .collect(Collectors.toList()));
        }

        return user;
    }
}
