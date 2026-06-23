package com.up.up_back.services;

import com.up.up_back.dto.subject.CreateSubjectDto;
import com.up.up_back.entity.Subject;
import com.up.up_back.entity.User;
import com.up.up_back.repository.SubjectRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubjectServiceTest {

    @Mock
    private SubjectRepository subjectRepository;

    @InjectMocks
    private SubjectServiceImpl subjectService;

    @Test
    void shouldCreateSubjectSuccessfully() {

        User user = User.builder()
                .id(1L)
                .name("Joao")
                .email("joao@gmail.com")
                .build();

        CreateSubjectDto dto = new CreateSubjectDto("Banco de Dados", 5);

        when(subjectRepository.save(any(Subject.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subject subject = subjectService.create(dto, user);

        assertNotNull(subject);

        assertEquals("Banco de Dados", subject.getName());
        assertEquals(5, subject.getDifficulty());
    }

    @Test
    void shouldAssociateSubjectWithUser() {

        User user = User.builder()
                .id(1L)
                .name("Joao")
                .email("joao@gmail.com")
                .build();

        CreateSubjectDto dto = new CreateSubjectDto("POO", 3);

        when(subjectRepository.save(any(Subject.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Subject subject = subjectService.create(dto, user);

        assertNotNull(subject.getUser());

        assertEquals(user.getId(), subject.getUser().getId());
    }
}
