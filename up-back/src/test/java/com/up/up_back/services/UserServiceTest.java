package com.up.up_back.services;

import com.up.up_back.dto.user.RegisterUserDto;
import com.up.up_back.dto.user.UserResponseDto;
import com.up.up_back.entity.User;
import com.up.up_back.exception.EmailAlreadyExistsException;
import com.up.up_back.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldCreateUserSuccessfully() {

        RegisterUserDto dto =
                new RegisterUserDto(
                        "Joao",
                        "joao@gmail.com",
                        "123456"
                );

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(dto.password())).thenReturn("encrypted");

        User savedUser = User.builder()
                        .id(1L)
                        .name(dto.name())
                        .email(dto.email())
                        .password("encrypted")
                        .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserResponseDto response = userService.create(dto);

        assertEquals(1L, response.id());
        assertEquals(dto.email(), response.email());

        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        RegisterUserDto dto = new RegisterUserDto(
                "joao",
                "joao@email.com",
                "123456"
        );

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.create(dto));
    }

    @Test
    void shouldEncryptPasswordBeforeSaving() {

        RegisterUserDto dto = new RegisterUserDto(
                "joao",
                "joao@email.com",
                "123456"
        );

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encrypted-password");

        User savedUser = User.builder()
                        .id(1L)
                        .name(dto.name())
                        .email(dto.email())
                        .password("encrypted-password")
                        .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        userService.create(dto);

        verify(passwordEncoder).encode("123456");
        verify(userRepository).save(argThat(user -> user.getPassword().equals("encrypted-password")));
    }
}
