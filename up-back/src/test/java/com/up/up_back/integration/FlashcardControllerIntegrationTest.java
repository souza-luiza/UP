package com.up.up_back.integration;

import com.up.up_back.dto.flashcard.CreateFlashcardDto;
import com.up.up_back.entity.Flashcard;
import com.up.up_back.entity.User;
import com.up.up_back.repository.AvailabilityRepository;
import com.up.up_back.repository.FlashcardRepository;
import com.up.up_back.repository.SubjectRepository;
import com.up.up_back.repository.UserRepository;
import com.up.up_back.security.JwtService;
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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"api.security.token.secret=chave_secreta_para_os_testes_de_integracao_do_projeto_up_com_mais_de_32_bytes"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class FlashcardControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlashcardRepository flashcardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Test
    void shouldCreateFlashcard() throws Exception {
        User user = User.builder()
                .name("Joao")
                .email("joao@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build();

        userRepository.save(user);

        String token = jwtService.generateAccessToken(user.getEmail());

        CreateFlashcardDto dto = new CreateFlashcardDto("Pergunta", "Resposta");

        mockMvc.perform(
                post("/flashcards")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.question").value("Pergunta"));
    }

    @Test
    void shouldListUserFlashcards() throws Exception {

        User user = User.builder()
                .name("Joao")
                .email("joao2@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build();

        userRepository.save(user);

        String token = jwtService.generateAccessToken(user.getEmail());

        flashcardRepository.save(
                Flashcard.builder()
                        .question("Pergunta")
                        .answer("Resposta")
                        .reviewLevel(0)
                        .nextReviewDate(LocalDate.now())
                        .user(user)
                        .build()
        );

        mockMvc.perform(
                    get("/flashcards")
                            .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void shouldReturnOnlyCardsDueForReview() throws Exception {

        User user = User.builder()
                .name("Joao")
                .email("joao3@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build();

        userRepository.save(user);

        String token = jwtService.generateAccessToken(user.getEmail());

        flashcardRepository.save(
                Flashcard.builder()
                        .question("Revisar hoje")
                        .answer("Resposta")
                        .reviewLevel(0)
                        .nextReviewDate(LocalDate.now())
                        .user(user)
                        .build()
        );

        flashcardRepository.save(
                Flashcard.builder()
                        .question("Revisar depois")
                        .answer("Resposta")
                        .reviewLevel(0)
                        .nextReviewDate(LocalDate.now().plusDays(5))
                        .user(user)
                        .build()
        );

        mockMvc.perform(
                        get("/flashcards/review")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].question").value("Revisar hoje"));
    }

    @Test
    void shouldReturnForbiddenWhenTokenIsMissing() throws Exception {

        mockMvc.perform(
                        get("/flashcards")
                )
                .andExpect(status().isForbidden());
    }
}
