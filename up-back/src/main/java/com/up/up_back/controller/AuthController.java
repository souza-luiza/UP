package com.up.up_back.controller;

import com.up.up_back.dto.auth.AuthTokensDto;
import com.up.up_back.dto.auth.LoginRequestDto;
import com.up.up_back.dto.auth.LoginResponseDto;
import com.up.up_back.exception.InvalidRefreshTokenException;
import com.up.up_back.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto) {

        AuthTokensDto tokens = authService.login(dto);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", tokens.refreshToken())
                .httpOnly(true)
                .secure(false) //MUDAR PARA DEPLOY
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();

        return ResponseEntity.ok()
                .header(
                    HttpHeaders.SET_COOKIE,
                    cookie.toString()
                ).body(
                        new LoginResponseDto(tokens.accessToken())
                );
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponseDto> refresh(@CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if(refreshToken == null) throw new InvalidRefreshTokenException("Refresh token not found");

        String accessToken = authService.refresh(refreshToken);

        return ResponseEntity.ok(new LoginResponseDto(accessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken) {

        if(refreshToken != null) authService.logout(refreshToken);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", "")
                .httpOnly(true)
                .secure(false) //MUDAR PARA DEPLOY
                .sameSite("Strict")
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();
    }

}
