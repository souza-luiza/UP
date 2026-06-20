package com.up.up_back.dto.auth;

public record LoginRequestDto(
        String email,
        String password
) {
}
