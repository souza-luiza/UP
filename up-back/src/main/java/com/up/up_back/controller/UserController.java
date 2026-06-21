package com.up.up_back.controller;

import com.up.up_back.dto.user.RegisterUserDto;
import com.up.up_back.dto.user.UserResponseDto;
import com.up.up_back.entity.User;
import com.up.up_back.security.UserDetailsImpl;
import com.up.up_back.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/me")
    public UserResponseDto me(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        User user = userDetails.getUser();

        return new UserResponseDto(user.getId(), user.getName(), user.getEmail());
    }
}
