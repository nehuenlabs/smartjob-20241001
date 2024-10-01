package com.example.domain.repository;

import com.example.domain.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    public Optional<User> findByEmail(String email);
}