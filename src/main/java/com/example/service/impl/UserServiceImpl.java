package com.example.service.impl;

import com.example.domain.repository.UserRepository;
import com.example.domain.repository.PhoneRepository;
import com.example.dto.UserDTO;
import com.example.dto.PhoneDTO;
import com.example.exception.RecordNotFoundException;
import com.example.exception.ValidationException;
import com.example.config.ErrorMessages;
import com.example.mapper.UserMapper;
import com.example.mapper.PhoneMapper;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PhoneRepository phoneRepository;

    private final Pattern emailPattern;
    private final Pattern passwordPattern;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PhoneRepository phoneRepository,
                           @Value("${validations.email}") String emailRegex,
                           @Value("${validations.password}") String passwordRegex) {
        this.userRepository = userRepository;
        this.phoneRepository = phoneRepository;
        this.emailPattern = Pattern.compile(emailRegex);
        this.passwordPattern = Pattern.compile(passwordRegex);
    }

    // Métodos para manejar usuarios

    @Override
    public UserDTO findUserById(UUID id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new RecordNotFoundException(ErrorMessages.USER_NOT_FOUND));
    }

    @Override
    public UserDTO findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new RecordNotFoundException(ErrorMessages.USER_NOT_FOUND));
    }

    @Override
    public UserDTO saveUser(UserDTO userDto) {
        validateUser(userDto);
        var user = UserMapper.toEntity(userDto);
        var savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    @Override
    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new RecordNotFoundException(ErrorMessages.USER_NOT_FOUND);
        }
        userRepository.deleteById(id);
    }

    // Métodos para manejar teléfonos

    @Override
    public List<PhoneDTO> findPhonesByUserId(UUID userId) {
        return phoneRepository.findByUserId(userId).stream()
                .map(PhoneMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PhoneDTO savePhone(PhoneDTO phoneDto) {
        validatePhone(phoneDto);
        var phone = PhoneMapper.toEntity(phoneDto);
        var savedPhone = phoneRepository.save(phone);
        return PhoneMapper.toDto(savedPhone);
    }

    @Override
    public void deletePhone(Long id) {
        if (!phoneRepository.existsById(id)) {
            throw new RecordNotFoundException(ErrorMessages.PHONE_NOT_FOUND);
        }
        phoneRepository.deleteById(id);
    }

    // Métodos de validación

    private void validateUser(UserDTO userDto) {
        if (userDto.getEmail() == null || userDto.getEmail().isEmpty() || !emailPattern.matcher(userDto.getEmail()).matches()) {
            throw new ValidationException(ErrorMessages.INVALID_DATA_EMAIL);
        }
        if (userDto.getPassword() == null || userDto.getPassword().isEmpty() || !passwordPattern.matcher(userDto.getPassword()).matches()) {
            throw new ValidationException(ErrorMessages.INVALID_DATA_PASSWORD);
        }
        // Agregar más validaciones según sea necesario
    }

    private void validatePhone(PhoneDTO phoneDto) {
        if (phoneDto.getNumber() == null || phoneDto.getNumber().isEmpty()) {
            throw new ValidationException(ErrorMessages.INVALID_DATA_PHONE);
        }
        // Agregar más validaciones según sea necesario
    }
}