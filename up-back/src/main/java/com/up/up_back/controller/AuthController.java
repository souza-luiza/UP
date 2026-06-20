package com.up.up_back.controller;

import com.up.up_back.dto.auth.AuthTokensDto;
import com.up.up_back.dto.auth.LoginRequestDto;
import com.up.up_back.dto.auth.LoginResponseDto;
import com.up.up_back.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
                .secure(true)
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

}
