package com.up.up_back.integration;

import com.up.up_back.dto.auth.LoginRequestDto;
import com.up.up_back.entity.User;
import com.up.up_back.repository.RefreshTokenRepository;
import com.up.up_back.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import tools.jackson.databind.ObjectMapper;


import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {"api.security.token.secret=chave_secreta_para_os_testes_de_integracao_do_projeto_up_com_mais_de_32_bytes"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {

        refreshTokenRepository.deleteAll();
        userRepository.deleteAll();

        User user = User.builder()
                .name("Joao")
                .email("joao@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build();

        userRepository.save(user);
    }

    @Test
    void shouldLoginSuccessfully() throws Exception {

        LoginRequestDto dto = new LoginRequestDto(
                "joao@gmail.com",
                "123456"
        );

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refreshToken=")));
    }

    @Test
    void shouldReturnUnauthorizedWhenPasswordIsInvalid() throws Exception {

        LoginRequestDto dto = new LoginRequestDto(
                        "joao@gmail.com",
                        "senha-errada"
                );

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldReturnUnauthorizedWhenUserDoesNotExist() throws Exception {

        LoginRequestDto dto = new LoginRequestDto(
                "inexistente@gmail.com",
                "123456"
        );

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRefreshAccessToken() throws Exception {

        LoginRequestDto dto = new LoginRequestDto(
                "joao@gmail.com",
                "123456"
        );

        MvcResult loginResult = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isOk())
                .andReturn();

        Cookie refreshCookie = loginResult.getResponse().getCookie("refreshToken");
        assertNotNull(refreshCookie);

        mockMvc.perform(
                post("/auth/refresh")
                        .cookie(refreshCookie)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists());
    }

    @Test
    void shouldReturnUnauthorizedWhenRefreshTokenIsMissing() throws Exception {

        mockMvc.perform(
                post("/auth/refresh")
        )
                .andExpect(status().isUnauthorized());
    }
}
