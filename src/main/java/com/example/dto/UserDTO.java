package com.example.dto;

import java.time.LocalDateTime;
import lombok.Data;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Data
@Builder
public class UserDTO {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private boolean isActive;
    private LocalDateTime created;
    private LocalDateTime modified;
    private LocalDateTime lastLogin;
    private List<PhoneDTO> phones;
}