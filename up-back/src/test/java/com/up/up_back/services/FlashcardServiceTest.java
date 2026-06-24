package com.up.up_back.services;

import com.up.up_back.dto.flashcard.CreateFlashcardDto;
import com.up.up_back.entity.Flashcard;
import com.up.up_back.entity.User;
import com.up.up_back.repository.FlashcardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceTest {

    @Mock
    private FlashcardRepository flashcardRepository;

    @InjectMocks
    private FlashcardServiceImpl flashcardService;

    @Test
    void shouldCreateFlashcardSuccessfully() {

        User user = User.builder()
                .id(1L)
                .build();

        CreateFlashcardDto dto = new CreateFlashcardDto(
                "O que é POO?",
                "Programação Orientada a Objetos"
        );

        when(flashcardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Flashcard flashcard = flashcardService.create(dto, user);

        assertEquals("O que é POO?", flashcard.getQuestion());
        assertEquals("Programação Orientada a Objetos", flashcard.getAnswer());
    }

    @Test
    void shouldAssociateFlashcardWithUser() {

        User user = User.builder()
                .id(1L)
                .build();

        CreateFlashcardDto dto = new CreateFlashcardDto(
                "Pergunta",
                "Resposta"
        );

        when(flashcardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Flashcard flashcard = flashcardService.create(dto, user);

        assertEquals(user.getId(), flashcard.getUser().getId());
    }

    @Test
    void shouldInitializeReviewLevelAndReviewDate() {

        User user = User.builder()
                .id(1L)
                .build();

        CreateFlashcardDto dto = new CreateFlashcardDto(
                "Pergunta",
                "Resposta"
        );

        when(flashcardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Flashcard flashcard = flashcardService.create(dto, user);
        assertEquals(0, flashcard.getReviewLevel());
        assertEquals(LocalDate.now(), flashcard.getNextReviewDate());
    }

    @Test
    void shouldCreateFlashcardDueForReviewToday() {

        User user = User.builder()
                .id(1L)
                .build();

        CreateFlashcardDto dto = new CreateFlashcardDto(
                "Pergunta",
                "Resposta"
        );

        when(flashcardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Flashcard flashcard = flashcardService.create(dto, user);
        assertFalse(flashcard.getNextReviewDate().isAfter(LocalDate.now()));
    }
}
