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
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void shouldReturnAllSubjectsFromUser() {

        User user = User.builder()
                .id(1L)
                .build();

        List<Subject> subjects = List.of(
                Subject.builder().name("POO").user(user).build(),
                Subject.builder().name("BD").user(user).build()
        );

        when(subjectRepository.findByUser(user))
                .thenReturn(subjects);

        List<Subject> result = subjectService.findAll(user);

        assertEquals(2, result.size());
        assertEquals(subjects, result);

        verify(subjectRepository).findByUser(user);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingSubject() {

        User user = User.builder()
                .id(1L)
                .build();

        when(subjectRepository.findById(100L))
                .thenReturn(Optional.empty());

        assertThrows(
                IllegalArgumentException.class,
                () -> subjectService.delete(100L, user)
        );

        verify(subjectRepository).findById(100L);
    }

    @Test
    void shouldNotAllowDeletingAnotherUsersSubject() {

        User owner = User.builder()
                .id(1L)
                .build();

        User anotherUser = User.builder()
                .id(2L)
                .build();

        Subject subject = Subject.builder()
                .user(owner)
                .build();

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        assertThrows(
                AccessDeniedException.class,
                () -> subjectService.delete(1L, anotherUser)
        );

        verify(subjectRepository, never()).delete(any());
    }

    @Test
    void shouldDeleteSubjectSuccessfully() {

        User user = User.builder()
                .id(1L)
                .build();

        Subject subject = Subject.builder()
                .id(1L)
                .user(user)
                .build();

        when(subjectRepository.findById(1L))
                .thenReturn(Optional.of(subject));

        subjectService.delete(1L, user);

        verify(subjectRepository).delete(subject);
    }
}
