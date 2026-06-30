package com.up.up_back.services;

import com.up.up_back.domain.ReviewResult;
import com.up.up_back.dto.flashcard.CreateFlashcardDto;
import com.up.up_back.entity.Flashcard;
import com.up.up_back.entity.User;
import com.up.up_back.exception.ForbiddenException;
import com.up.up_back.repository.FlashcardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FlashcardServiceTest {

    @Mock
    private FlashcardRepository flashcardRepository;

    @Mock
    private FlashcardReviewService flashcardReviewService;

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

    @Test
    void shouldIncreaseLevelWhenReviewIsCorrect() {

        User user = User.builder()
                .id(1L)
                .build();

        Flashcard flashcard = Flashcard.builder()
                .id(1L)
                .question("Pergunta")
                .answer("Resposta")
                .reviewLevel(0)
                .user(user)
                .build();

        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(flashcard));
        when(flashcardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate expectedDate = LocalDate.now().plusDays(3);

        when(flashcardReviewService.review(anyInt(), anyBoolean())).thenReturn(new ReviewResult(1, LocalDate.now().plusDays(3)));

        Flashcard updated = flashcardService.review(1L, true, user);

        assertEquals(1, updated.getReviewLevel());
        assertEquals(expectedDate, updated.getNextReviewDate());
    }

    @Test
    void shouldResetLevelWhenReviewIsWrong() {

        User user = User.builder()
                .id(1L)
                .build();

        Flashcard flashcard = Flashcard.builder()
                .id(1L)
                .question("Pergunta")
                .answer("Resposta")
                .reviewLevel(3)
                .user(user)
                .build();

        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(flashcard));
        when(flashcardRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDate expectedDate = LocalDate.now().plusDays(1);

        when(flashcardReviewService.review(anyInt(), anyBoolean())).thenReturn(new ReviewResult(0, LocalDate.now().plusDays(1)));

        Flashcard updated = flashcardService.review(1L, false, user);

        assertEquals(0, updated.getReviewLevel());
        assertEquals(expectedDate, updated.getNextReviewDate());
    }

    @Test
    void shouldNotAllowUserToReviewAnotherUsersFlashcard() {

        User owner = User.builder()
                .id(1L)
                .build();

        User attacker = User.builder()
                .id(2L)
                .build();

        Flashcard flashcard = Flashcard.builder()
                .id(1L)
                .user(owner)
                .build();

        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(flashcard));

        assertThrows(
                ForbiddenException.class,
                () -> flashcardService.review(1L, true, attacker)
        );
    }

    @Test
    void shouldDeleteFlashcardSuccessfully() {

        User user = User.builder()
                .id(1L)
                .build();

        Flashcard flashcard = Flashcard.builder()
                .id(1L)
                .user(user)
                .build();

        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(flashcard));

        flashcardService.delete(1L, user);

        verify(flashcardRepository).delete(flashcard);
    }

    @Test
    void shouldNotDeleteAnotherUsersFlashcard() {

        User owner = User.builder()
                .id(1L)
                .build();

        User attacker = User.builder()
                .id(2L)
                .build();

        Flashcard flashcard = Flashcard.builder()
                .id(1L)
                .user(owner)
                .build();

        when(flashcardRepository.findById(1L)).thenReturn(Optional.of(flashcard));

        assertThrows(
                ForbiddenException.class,
                () -> flashcardService.delete(1L, attacker)
        );
    }

    @Test
    void shouldReturnAllFlashcardsFromUser() {

        User user = User.builder()
                .id(1L)
                .build();

        Flashcard flashcard = Flashcard.builder()
                .id(1L)
                .user(user)
                .build();

        when(flashcardRepository.findByUser(user))
                .thenReturn(List.of(flashcard));

        List<Flashcard> result = flashcardService.findAllByUser(user);

        assertEquals(1, result.size());
        assertEquals(flashcard, result.getFirst());

        verify(flashcardRepository).findByUser(user);
    }

    @Test
    void shouldReturnCardsDueForReview() {

        User user = User.builder()
                .id(1L)
                .build();

        Flashcard flashcard = Flashcard.builder()
                .id(1L)
                .user(user)
                .nextReviewDate(LocalDate.now())
                .build();

        when(flashcardRepository.findByUserAndNextReviewDateLessThanEqual(
                eq(user),
                any(LocalDate.class)
        )).thenReturn(List.of(flashcard));

        List<Flashcard> result = flashcardService.findCardsDueForReview(user);

        assertEquals(1, result.size());
        assertEquals(flashcard, result.getFirst());

        verify(flashcardRepository)
                .findByUserAndNextReviewDateLessThanEqual(eq(user), any(LocalDate.class));
    }

    @Test
    void shouldThrowExceptionWhenFlashcardDoesNotExistForReview() {

        User user = User.builder()
                .id(1L)
                .build();

        when(flashcardRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> flashcardService.review(999L, true, user)
        );
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonexistentFlashcard() {

        User user = User.builder()
                .id(1L)
                .build();

        when(flashcardRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> flashcardService.delete(999L, user)
        );
    }
}
