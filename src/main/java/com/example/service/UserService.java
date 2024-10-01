package com.example.service;

import com.example.dto.UserDTO;
import com.example.dto.PhoneDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // Métodos para manejar usuarios
    UserDTO findUserById(UUID id);

    UserDTO findUserByEmail(String email);

    UserDTO saveUser(UserDTO userDto);

    void deleteUser(UUID id);

    // Métodos para manejar teléfonos
    List<PhoneDTO> findPhonesByUserId(UUID userId);

    PhoneDTO savePhone(PhoneDTO phoneDto);

    void deletePhone(Long id);
}