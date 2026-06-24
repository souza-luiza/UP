package com.up.up_back.controller;

import com.up.up_back.dto.flashcard.CreateFlashcardDto;
import com.up.up_back.dto.flashcard.FlashcardResponseDto;
import com.up.up_back.entity.Flashcard;
import com.up.up_back.entity.User;
import com.up.up_back.security.UserDetailsImpl;
import com.up.up_back.services.FlashcardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flashcards")
@RequiredArgsConstructor
public class FlashcardController {

    private final FlashcardService flashcardService;

    private FlashcardResponseDto toResponse(Flashcard flashcard) {
        return new FlashcardResponseDto(
                flashcard.getId(),
                flashcard.getQuestion(),
                flashcard.getAnswer(),
                flashcard.getReviewLevel(),
                flashcard.getNextReviewDate()
        );
    }

    @PostMapping
    public FlashcardResponseDto create(@RequestBody @Valid CreateFlashcardDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Flashcard flashcard = flashcardService.create(dto, userDetails.getUser());

        return toResponse(flashcard);
    }

    @GetMapping
    public List<FlashcardResponseDto> findAll(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        return flashcardService
                .findAllByUser(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/review")
    public List<FlashcardResponseDto> dueForReview(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        return flashcardService
                .findCardsDueForReview(user)
                .stream()
                .map(this::toResponse)
                .toList();
    }

}
