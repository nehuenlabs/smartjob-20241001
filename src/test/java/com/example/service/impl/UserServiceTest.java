package com.example.service.impl;

import com.example.domain.model.Phone;
import com.example.domain.repository.UserRepository;
import com.example.domain.repository.PhoneRepository;
import com.example.dto.UserDTO;
import com.example.dto.PhoneDTO;
import com.example.exception.RecordNotFoundException;
import com.example.exception.ValidationException;
import com.example.mapper.UserMapper;
import com.example.mapper.PhoneMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneRepository phoneRepository;

    private UserServiceImpl userService;

    // Expresiones regulares simuladas
    private final String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private final String passwordRegex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$";

    @BeforeEach
    void setUp() {
//        userRepository = Mockito.mock(UserRepository.class);
//        phoneRepository = Mockito.mock(PhoneRepository.class);
        userService = new UserServiceImpl(userRepository, phoneRepository, emailRegex, passwordRegex);
    }

    @Test
    void findUserById_ShouldReturnUserDTO_WhenUserExists() {
        UUID userId = UUID.randomUUID();
        var user = UserMapper.toEntity(createUserDTO(userId));

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO foundUserDto = userService.findUserById(userId);

        assertThat(foundUserDto).isEqualTo(UserMapper.toDto(user));
        verify(userRepository).findById(userId);
    }

    @Test
    void findUserById_ShouldThrowException_WhenUserDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserById(userId))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findById(userId);
    }

    @Test
    void findUserByEmail_ShouldReturnUserDTO_WhenUserExists() {
        String email = "john@example.com";
        var user = UserMapper.toEntity(createUserDTO(UUID.randomUUID()));

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDTO foundUserDto = userService.findUserByEmail(email);

        assertThat(foundUserDto).isEqualTo(UserMapper.toDto(user));
        verify(userRepository).findByEmail(email);
    }

    @Test
    void findUserByEmail_ShouldThrowException_WhenUserDoesNotExist() {
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserByEmail(email))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userRepository).findByEmail(email);
    }

    @Test
    void saveUser_ShouldSaveAndReturnUserDTO_WhenValid() {
        UserDTO userDto = createUserDTO(UUID.randomUUID());

        var user = UserMapper.toEntity(userDto);

        when(userRepository.save(any())).thenReturn(user);

        UserDTO savedUserDto = userService.saveUser(userDto);

        assertThat(savedUserDto).isEqualTo(UserMapper.toDto(user));
        verify(userRepository).save(any());
    }

    @Test
    void saveUser_ShouldThrowException_WhenEmailIsInvalid() {
        UserDTO userDto = createInvalidEmailUserDTO();

        assertThatThrownBy(() -> userService.saveUser(userDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid email.");

        verifyNoInteractions(userRepository);
    }

    @Test
    void saveUser_ShouldThrowException_WhenPasswordIsInvalid() {
        UserDTO userDto = createInvalidPasswordUserDTO();

        assertThatThrownBy(() -> userService.saveUser(userDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid password.");

        verifyNoInteractions(userRepository);
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenExists() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(any(UUID.class))).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(any(UUID.class));
    }

    @Test
    void deleteUser_ShouldThrowException_WhenDoesNotExist() {
        UUID userId = UUID.randomUUID();

        when(userRepository.existsById(any(UUID.class))).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class, () -> userService.deleteUser(userId));
    }

    @Test
    void savePhone_ShouldSaveAndReturnPhoneDTO_WhenValid() {
        PhoneDTO phoneDto = createValidPhoneDTO();

        var phone = PhoneMapper.toEntity(phoneDto);

        when(phoneRepository.save(any())).thenReturn(phone);

        PhoneDTO savedPhoneDto = userService.savePhone(phoneDto);

        assertThat(savedPhoneDto).isEqualTo(PhoneMapper.toDto(phone));
        verify(phoneRepository).save(any());
    }

    @Test
    void savePhone_ShouldThrowException_WhenNumberIsInvalid() {
        PhoneDTO phoneDto = createInvalidNumberPhoneDTO();

        assertThatThrownBy(() -> userService.savePhone(phoneDto))
                .isInstanceOf(ValidationException.class)
                .hasMessageContaining("Invalid phone.");

        verifyNoInteractions(phoneRepository);
    }

    @Test
    void deletePhone_ShouldDeletePhone_WhenExists() {
        Long phoneId = 1L;

        when(phoneRepository.existsById(anyLong())).thenReturn(true);

        userService.deletePhone(phoneId);

        verify(phoneRepository).deleteById(anyLong());
    }

    @Test
    void deletePhone_ShouldThrowException_WhenDoesNotExist() {
        Long phoneId = 1L;

        when(phoneRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> userService.deletePhone(phoneId))
                .isInstanceOf(RecordNotFoundException.class)
                .hasMessageContaining("Phone not found");

        verifyNoMoreInteractions(phoneRepository);
    }

// Métodos auxiliares para crear DTOs y listas
    private UserDTO createUserDTO(UUID id) {
        return UserDTO.builder()
                .id(id)
                .name("John Doe")
                .email("john@example.com")
                .password("Pass1234,.")
                .isActive(true)
                .phones(createPhoneDTOList())
                .build();
    }

    private UserDTO createInvalidEmailUserDTO() {
        return UserDTO.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .email("invalid-email")
                .isActive(true)
                .build();
    }

    private UserDTO createInvalidPasswordUserDTO() {
        return UserDTO.builder()
                .id(UUID.randomUUID())
                .name("Jane Doe")
                .email("jane@example.com")
                .isActive(true)
                .build();
    }

    private PhoneDTO createValidPhoneDTO() {
        return PhoneDTO.builder()
                .id(1L)
                .number("1234567890")
                .citycode("123")
                .countrycode("1")
                .build();
    }

    private PhoneDTO createInvalidNumberPhoneDTO() {
        return PhoneDTO.builder()
                .id(1L)
                .number("")
                .citycode("123")
                .countrycode("1")
                .build();
    }

    private List<Phone> createPhoneList() {
        return List.of(
                Phone.builder()
                        .id(1L)
                        .number("1234567890")
                        .citycode("123")
                        .countrycode("1")
                        .build(),
                Phone.builder()
                        .id(2L)
                        .number("0987654321")
                        .citycode("321")
                        .countrycode("2")
                        .build()
        );
    }

    private List<PhoneDTO> createPhoneDTOList() {
        return List.of(
                PhoneDTO.builder()
                        .id(1L)
                        .number("1234567890")
                        .citycode("123")
                        .countrycode("1")
                        .build(),
                PhoneDTO.builder()
                        .id(2L)
                        .number("0987654321")
                        .citycode("321")
                        .countrycode("2")
                        .build()
        );
    }
}
