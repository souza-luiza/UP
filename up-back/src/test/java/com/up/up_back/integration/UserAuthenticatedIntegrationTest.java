package com.up.up_back.integration;

import com.up.up_back.entity.User;
import com.up.up_back.repository.UserRepository;
import com.up.up_back.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"api.security.token.secret=chave_secreta_para_os_testes_de_integracao_do_projeto_up_com_mais_de_32_bytes"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class UserAuthenticatedIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setup() {

        userRepository.deleteAll();

        User user = User.builder()
                .name("Joao")
                .email("joao@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build();

        userRepository.save(user);
    }

    @Test
    void shouldReturnForbiddenWhenTokenIsMissing() throws Exception {

        mockMvc.perform(
                get("/users/me")
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnCurrentUserWhenTokenIsValid() throws Exception {

        String token = jwtService.generateAccessToken("joao@gmail.com");

        mockMvc.perform(
                get("/users/me")
                        .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("joao@gmail.com"));
    }

    @Test
    void shouldReturnForbiddenWhenTokenIsInvalid() throws Exception {

        mockMvc.perform(
                get("/users/me")
                        .header("Authorization", "Bearer token-invalido")
        )
                .andExpect(status().isForbidden());
    }
}
