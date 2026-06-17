package com.up.up_back.dto;

public record RegisterUserDto(
        String name,
        String email,
        String password
) {
}
