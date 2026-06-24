package com.up.up_back.services;

import com.up.up_back.dto.flashcard.CreateFlashcardDto;
import com.up.up_back.entity.Flashcard;
import com.up.up_back.entity.User;
import com.up.up_back.repository.FlashcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {

    private final FlashcardRepository flashcardRepository;

    @Override
    public Flashcard create(CreateFlashcardDto dto, User user) {

        Flashcard flashcard = Flashcard.builder()
                .question(dto.question())
                .answer(dto.answer())
                .reviewLevel(0)
                .nextReviewDate(LocalDate.now())
                .user(user)
                .build();

        return flashcardRepository.save(flashcard);
    }
}
