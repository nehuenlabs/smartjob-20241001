package com.example.domain.repository;

import com.example.domain.model.User;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class JPAUnitTest {
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void whenSaveUser_thenUserIsSaved() {
        // given
        User user = User.builder()
                .name("John Doe")
                .email("john@example.com")
                .password("password123")
                .isActive(true)
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    void whenFindByEmail_thenReturnUser() {
        // given
        User user = User.builder()
                .name("Jane Doe")
                .email("jane@example.com")
                .password("password456")
                .isActive(true)
                .build();
        userRepository.save(user);

        // when
        Optional<User> found = userRepository.findByEmail("jane@example.com");

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Jane Doe");
    }

    @Test
    void whenDeleteUser_thenUserShouldNotExist() {
        // given
        User user = User.builder()
                .name("Alice")
                .email("alice@example.com")
                .password("password789")
                .isActive(true)
                .build();
        userRepository.save(user);

        // when
        userRepository.delete(user);

        // then
        Optional<User> deletedUser = userRepository.findById(user.getId());
        assertThat(deletedUser).isEmpty();
    }    
}