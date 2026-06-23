package com.up.up_back.integration;

import com.up.up_back.dto.subject.CreateSubjectDto;
import com.up.up_back.entity.Subject;
import com.up.up_back.entity.User;
import com.up.up_back.repository.SubjectRepository;
import com.up.up_back.repository.UserRepository;
import com.up.up_back.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"api.security.token.secret=chave_secreta_para_os_testes_de_integracao_do_projeto_up_com_mais_de_32_bytes"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class SubjectControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SubjectRepository subjectRepository;

    private User savedUser;
    private String token;

    @BeforeEach
    void setup() {
        subjectRepository.deleteAll();

        User user = User.builder()
                .name("Joao")
                .email("joao@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build();

        this.savedUser = userRepository.save(user);
        this.token = jwtService.generateAccessToken(user.getEmail());
    }

    @Test
    void shouldReturnForbiddenWhenTokenIsMissing() throws Exception {

        CreateSubjectDto dto = new CreateSubjectDto("Banco de Dados", 5);

        mockMvc.perform(
                post("/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldCreateSubjectWhenAuthenticated() throws Exception {

        CreateSubjectDto dto = new CreateSubjectDto("Banco de Dados", 5);

        mockMvc.perform(
                post("/subjects")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isOk());

        List<Subject> subjects = subjectRepository.findAll();
        assertEquals(1, subjects.size());

        Subject subject = subjects.getFirst();
        assertEquals("Banco de Dados", subject.getName());
        assertEquals(5, subject.getDifficulty());
        assertEquals(savedUser.getId(), subject.getUser().getId());
    }

    @Test
    void shouldReturnBadRequestWhenDifficultyIsLessThanOne() throws Exception {

        CreateSubjectDto dto = new CreateSubjectDto("Banco de Dados", 0);

        mockMvc.perform(
                        post("/subjects")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenDifficultyIsGreaterThanFive() throws Exception {

        CreateSubjectDto dto = new CreateSubjectDto("Banco de Dados", 6);

        mockMvc.perform(
                        post("/subjects")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

}
