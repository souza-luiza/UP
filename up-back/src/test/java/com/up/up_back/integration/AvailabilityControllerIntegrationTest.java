package com.up.up_back.integration;

import com.up.up_back.dto.availability.CreateAvailabilityDto;
import com.up.up_back.dto.subject.CreateSubjectDto;
import com.up.up_back.entity.Availability;
import com.up.up_back.entity.Subject;
import com.up.up_back.entity.User;
import com.up.up_back.repository.AvailabilityRepository;
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

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"api.security.token.secret=chave_secreta_para_os_testes_de_integracao_do_projeto_up_com_mais_de_32_bytes"})
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class AvailabilityControllerIntegrationTest {

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
    private AvailabilityRepository availabilityRepository;

    private User savedUser;
    private String token;

    @BeforeEach
    void setup() {
        availabilityRepository.deleteAll();

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

        CreateAvailabilityDto dto = new CreateAvailabilityDto(DayOfWeek.MONDAY, LocalTime.of(19, 0), LocalTime.of(22, 0));

        mockMvc.perform(
                post("/availabilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        )
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldCreateAvailabilityWhenAuthenticated() throws Exception {

        CreateAvailabilityDto dto = new CreateAvailabilityDto(DayOfWeek.MONDAY, LocalTime.of(19, 0), LocalTime.of(22, 0));

        mockMvc.perform(
                        post("/availabilities")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isOk());

        List<Availability> availabilities = availabilityRepository.findAll();
        assertEquals(1, availabilities.size());

        Availability availability = availabilities.getFirst();
        assertEquals(DayOfWeek.MONDAY, availability.getDayOfWeek());
        assertEquals(LocalTime.of(19, 0), availability.getStartTime());
        assertEquals(LocalTime.of(22, 0), availability.getEndTime());
        assertEquals(savedUser.getId(), availability.getUser().getId());
    }

    @Test
    void shouldReturnBadRequestWhenEndTimeIsBeforeStartTime() throws Exception {

        CreateAvailabilityDto dto = new CreateAvailabilityDto(DayOfWeek.MONDAY, LocalTime.of(22, 0), LocalTime.of(19, 0));

        mockMvc.perform(
                        post("/availabilities")
                                .header("Authorization", "Bearer " + token)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto))
                )
                .andExpect(status().isBadRequest());
    }

}
