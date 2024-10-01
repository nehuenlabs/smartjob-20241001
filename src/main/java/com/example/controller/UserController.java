package com.example.controller;

import com.example.dto.UserDTO;
import com.example.dto.PhoneDTO;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.findUserById(id));
    }

    @GetMapping("/users/email/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findUserByEmail(email));
    }

    @PostMapping("/users")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDto) {
        var savedUser = userService.saveUser(userDto);
        return new ResponseEntity<>((savedUser), HttpStatus.CREATED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{userId}/phones")
    public ResponseEntity<List<PhoneDTO>> getPhonesByUserId(@PathVariable UUID userId) {
        var phones = userService.findPhonesByUserId(userId);
        return ResponseEntity.ok(phones);
    }

    @PostMapping("/phones")
    public ResponseEntity<PhoneDTO> createPhone(@RequestBody PhoneDTO phoneDto) {
        var savedPhone = userService.savePhone(phoneDto);
        return new ResponseEntity<>(savedPhone, HttpStatus.CREATED);
    }

    @DeleteMapping("/phones/{id}")
    public ResponseEntity<Void> deletePhone(@PathVariable Long id) {
        userService.deletePhone(id);
        return ResponseEntity.noContent().build();
    }
}