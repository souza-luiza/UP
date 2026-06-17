package com.up.up_back.services;

import com.up.up_back.dto.RegisterUserDto;
import com.up.up_back.entity.User;
import com.up.up_back.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User create(RegisterUserDto dto) {

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .password(dto.password())
                .build();

        return userRepository.save(user);
    }
}
