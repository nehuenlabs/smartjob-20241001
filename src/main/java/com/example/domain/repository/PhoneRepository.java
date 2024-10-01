package com.example.domain.repository;

import com.example.domain.model.Phone;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    
    List<Phone> findByUserId(UUID userId);

}