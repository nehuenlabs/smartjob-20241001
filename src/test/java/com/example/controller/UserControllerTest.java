package com.example.controller;

import com.example.dto.UserDTO;
import com.example.dto.PhoneDTO;
import com.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserDTO userDto;
    private PhoneDTO phoneDto;

    @BeforeEach
    void setUp() {
        userDto = UserDTO.builder()
                .id(UUID.randomUUID())
                .name("John Doe")
                .email("john@example.com")
                .isActive(true)
                .phones(Collections.emptyList())
                .build();

        phoneDto = PhoneDTO.builder()
                .id(1L)
                .number("1234567890")
                .citycode("123")
                .countrycode("1")
                .build();
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        when(userService.findUserById(any(UUID.class))).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/users/{id}", userDto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto.getId().toString())))
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() throws Exception {
        when(userService.findUserByEmail(anyString())).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/users/email/{email}", userDto.getEmail())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void createUser_ShouldReturnCreatedStatus_WhenUserIsValid() throws Exception {
        when(userService.saveUser(any(UserDTO.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"isActive\":true}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(userDto.getName())))
                .andExpect(jsonPath("$.email", is(userDto.getEmail())));
    }

    @Test
    void deleteUser_ShouldReturnNoContent_WhenUserExists() throws Exception {
        doNothing().when(userService).deleteUser(any(UUID.class));

        mockMvc.perform(delete("/api/v1/users/{id}", userDto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void getPhonesByUserId_ShouldReturnPhones_WhenPhonesExist() throws Exception {
        when(userService.findPhonesByUserId(any(UUID.class))).thenReturn(Collections.singletonList(phoneDto));

        mockMvc.perform(get("/api/v1/users/{userId}/phones", userDto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].number", is(phoneDto.getNumber())));
    }

    @Test
    void createPhone_ShouldReturnCreatedStatus_WhenPhoneIsValid() throws Exception {
        when(userService.savePhone(any(PhoneDTO.class))).thenReturn(phoneDto);

        mockMvc.perform(post("/api/v1/phones")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"number\":\"1234567890\",\"citycode\":\"123\",\"countrycode\":\"1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.number", is(phoneDto.getNumber())));
    }

    @Test
    void deletePhone_ShouldReturnNoContent_WhenPhoneExists() throws Exception {
        doNothing().when(userService).deletePhone(anyLong());

        mockMvc.perform(delete("/api/v1/phones/{id}", phoneDto.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
