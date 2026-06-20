package com.up.up_back.dto.auth;

public record AuthTokensDto(
        String accessToken,
        String refreshToken
) {
}
