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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthTokensDto login(LoginRequestDto dto) {

        User user = userRepository.findByEmail(dto.email()).orElseThrow(
                () -> new InvalidCredentialsException("Invalid credentials")
        );

        boolean passwordMatches = passwordEncoder.matches(dto.password(), user.getPassword());
        if(!passwordMatches) throw new InvalidCredentialsException("Invalid credentials");

        String accessToken = jwtService.generateAccessToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .token(refreshToken)
                        .expiresAt(Instant.now().plusSeconds(60 * 60 * 24 * 7))
                        .user(user)
                        .build()
        );

        return new AuthTokensDto(accessToken, refreshToken);
    }

    public String refresh(String refreshToken) {

        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(
                        () -> new InvalidRefreshTokenException("Invalid refresh token")
                );

        if(storedToken.getExpiresAt().isBefore(Instant.now())) throw new InvalidRefreshTokenException("Refresh token expired");

        return jwtService.generateAccessToken(storedToken.getUser().getEmail());
    }

    @Transactional
    public void logout(String refreshToken) {

        refreshTokenRepository.deleteByToken(refreshToken);
    }
}
