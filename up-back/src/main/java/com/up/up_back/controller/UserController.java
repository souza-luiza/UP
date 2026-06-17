package com.up.up_back.controller;

import com.up.up_back.dto.RegisterUserDto;
import com.up.up_back.entity.User;
import com.up.up_back.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody RegisterUserDto dto) {
        return userService.create(dto);
    }
}
