package com.up.up_back.integration;

import com.up.up_back.entity.Availability;
import com.up.up_back.entity.Subject;
import com.up.up_back.entity.User;
import com.up.up_back.repository.AvailabilityRepository;
import com.up.up_back.repository.SubjectRepository;
import com.up.up_back.repository.UserRepository;
import com.up.up_back.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.time.DayOfWeek;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"api.security.token.secret=chave_secreta_para_os_testes_de_integracao_do_projeto_up_com_mais_de_32_bytes"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ScheduleControllerIntegrationTest {

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

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Test
    void shouldGenerateScheduleForAuthenticatedUser() throws Exception {

        User user = User.builder()
                .name("Joao")
                .email("joao@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build();

        userRepository.save(user);

        Subject bd = Subject.builder()
                .name("Banco de Dados")
                .difficulty(5)
                .user(user)
                .build();

        subjectRepository.save(bd);

        Availability availability = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .startTime(LocalTime.of(19, 0))
                .endTime(LocalTime.of(22, 0))
                .user(user)
                .build();

        availabilityRepository.save(availability);

        String token = jwtService.generateAccessToken(user.getEmail());

        mockMvc.perform(
                post("/schedule/generate")
                        .header("Authorization", "Bearer " + token)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sessions").isArray())
                .andExpect(jsonPath("$.sessions.length()").value(1))
                .andExpect(jsonPath("$.sessions[0].subjectName").value("Banco de Dados"));
    }

    @Test
    void shouldReturnForbiddenWhenTokenIsMissing() throws Exception {

        mockMvc.perform(
                        post("/schedule/generate")
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldReturnBadRequestWhenThereIsNotEnoughAvailableTime() throws Exception {

        User user = User.builder()
                .name("Joao")
                .email("joao2@gmail.com")
                .password(passwordEncoder.encode("123456"))
                .build();

        userRepository.save(user);

        subjectRepository.save(
                Subject.builder()
                .name("Banco de Dados")
                .difficulty(5)
                .user(user)
                .build()
        );

        subjectRepository.save(
                Subject.builder()
                        .name("POO")
                        .difficulty(3)
                        .user(user)
                        .build()
        );

        subjectRepository.save(
                Subject.builder()
                        .name("Calculo")
                        .difficulty(4)
                        .user(user)
                        .build()
        );

        availabilityRepository.save(
                Availability.builder()
                    .dayOfWeek(DayOfWeek.MONDAY)
                    .startTime(LocalTime.of(19, 0))
                    .endTime(LocalTime.of(20, 0))
                    .user(user)
                    .build()
        );

        String token = jwtService.generateAccessToken(user.getEmail());

        mockMvc.perform(
                        post("/schedule/generate")
                                .header("Authorization", "Bearer " + token)
                )
                .andExpect(status().isBadRequest());
    }
}
