package com.up.up_back.services;

import com.up.up_back.dto.auth.AuthTokensDto;
import com.up.up_back.dto.auth.LoginRequestDto;
import com.up.up_back.entity.RefreshToken;
import com.up.up_back.entity.User;
import com.up.up_back.exception.InvalidCredentialsException;
import com.up.up_back.exception.InvalidRefreshTokenException;
import com.up.up_back.repository.RefreshTokenRepository;
import com.up.up_back.repository.UserRepository;
import com.up.up_back.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldLoginSuccessfully() {

        LoginRequestDto dto = new LoginRequestDto(
                "mariazinha@gmail.com",
                "123456"
        );

        User user = User.builder()
                .id(1L)
                .name("Maria")
                .email("mariazinha@gmail.com")
                .password("encrypted-password")
                .build();

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(user.getEmail())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user.getEmail())).thenReturn("refresh-token");

        AuthTokensDto response = authService.login(dto);

        assertNotNull(response);

        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotExist() {

        LoginRequestDto dto = new LoginRequestDto(
                "maria@gmail.com",
                "123456"
        );

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.empty());
        assertThrows(InvalidCredentialsException.class, () -> authService.login(dto));

        verify(refreshTokenRepository, never()).save(any());
        verify(jwtService, never()).generateAccessToken(any());
        verify(jwtService, never()).generateRefreshToken(any());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {

        LoginRequestDto dto = new LoginRequestDto(
                "joao@gmail.com",
                "senha-errada"
        );

        User user = User.builder()
                .id(1L)
                .name("Joao")
                .email("joao@gmail.com")
                .password("encrypted-password")
                .build();

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(dto));

        verify(refreshTokenRepository, never()).save(any());
        verify(jwtService, never()).generateAccessToken(any());
        verify(jwtService, never()).generateRefreshToken(any());
    }

    @Test
    void shouldSaveRefreshToken() {

        LoginRequestDto dto = new LoginRequestDto(
                "joao@gmail.com",
                "123456"
        );

        User user = User.builder()
                .id(1L)
                .name("Joao")
                .email("joao@gmail.com")
                .password("encrypted-password")
                .build();

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(user.getEmail())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user.getEmail())).thenReturn("refresh-token");

        authService.login(dto);

        verify(refreshTokenRepository).save(any());
    }

    @Test
    void shouldGenerateTokens() {

        LoginRequestDto dto = new LoginRequestDto(
                "joao@gmail.com",
                "123456"
        );

        User user = User.builder()
                .id(1L)
                .name("Joao")
                .email("joao@gmail.com")
                .password("encrypted-password")
                .build();

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(user.getEmail())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user.getEmail())).thenReturn("refresh-token");

        authService.login(dto);

        verify(jwtService).generateAccessToken(user.getEmail());
        verify(jwtService).generateRefreshToken(user.getEmail());
    }

    @Test
    void shouldSaveRefreshTokenWithCorrectUserAndTokenValue() {

        LoginRequestDto dto = new LoginRequestDto(
                "joao@gmail.com",
                "123456"
        );

        User user = User.builder()
                .id(1L)
                .name("Joao")
                .email("joao@gmail.com")
                .password("encrypted-password")
                .build();

        when(userRepository.findByEmail(dto.email())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(dto.password(), user.getPassword())).thenReturn(true);
        when(jwtService.generateAccessToken(user.getEmail())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(user.getEmail())).thenReturn("refresh-token");

        ArgumentCaptor<RefreshToken> captor = ArgumentCaptor.forClass(RefreshToken.class);

        authService.login(dto);

        verify(refreshTokenRepository).save(captor.capture());

        RefreshToken savedToken = captor.getValue();

        assertNotNull(savedToken);
        assertEquals("refresh-token", savedToken.getToken());
        assertEquals(user, savedToken.getUser());
    }

    @Test
    void shouldGenerateNewAccessTokenFromRefreshToken() {

        User user = User.builder()
                .id(1L)
                .name("Joao")
                .email("joao@gmail.com")
                .password("encrypted-password")
                .build();

        RefreshToken refreshToken =  RefreshToken.builder()
                .token("refresh-token")
                .user(user)
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        when(refreshTokenRepository.findByToken("refresh-token")).thenReturn(Optional.of(refreshToken));
        when(jwtService.generateAccessToken(user.getEmail())).thenReturn("new-access-token");

        String accessToken = authService.refresh("refresh-token");
        assertEquals("new-access-token", accessToken);
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenDoesNotExist() {

        when(refreshTokenRepository.findByToken("invalid-token")).thenReturn(Optional.empty());
        assertThrows(InvalidRefreshTokenException.class, () -> authService.refresh("invalid-token"));
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenIsExpired() {

        User user = User.builder()
                .id(1L)
                .name("Joao")
                .email("joao@gmail.com")
                .password("encrypted-password")
                .build();

        RefreshToken refreshToken =  RefreshToken.builder()
                .token("refresh-token")
                .user(user)
                .expiresAt(Instant.now().minusSeconds(60))
                .build();

        when(refreshTokenRepository.findByToken("refresh-token")).thenReturn(Optional.of(refreshToken));
        assertThrows(InvalidRefreshTokenException.class, () -> authService.refresh("refresh-token"));
    }

    @Test
    void shouldDeleteRefreshTokenOnLogout() {

        authService.logout("refresh-token");
        verify(refreshTokenRepository).deleteByToken("refresh-token");
    }
}
