package com.up.up_back.controller;

import com.up.up_back.dto.user.RegisterUserDto;
import com.up.up_back.dto.user.UserResponseDto;
import com.up.up_back.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public UserResponseDto create(@Valid @RequestBody RegisterUserDto dto) {
        return userService.create(dto);
    }
}
