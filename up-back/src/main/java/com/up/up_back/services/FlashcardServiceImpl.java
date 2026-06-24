package com.up.up_back.services;

import com.up.up_back.domain.ReviewResult;
import com.up.up_back.dto.flashcard.CreateFlashcardDto;
import com.up.up_back.entity.Flashcard;
import com.up.up_back.entity.User;
import com.up.up_back.exception.ForbiddenException;
import com.up.up_back.repository.FlashcardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService {

    private final FlashcardRepository flashcardRepository;

    private final FlashcardReviewService flashcardReviewService;

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

    @Override
    public List<Flashcard> findAllByUser(User user) {

        return flashcardRepository.findByUser(user);
    }

    @Override
    public List<Flashcard> findCardsDueForReview(User user) {

        return flashcardRepository.findByUserAndNextReviewDateLessThanEqual(user, LocalDate.now());
    }

    @Override
    public Flashcard review(Long flashcardId, boolean correct, User user) {

        Flashcard flashcard =
                flashcardRepository.findById(flashcardId)
                        .orElseThrow(
                                () -> new RuntimeException("Flashcard not found")
                        );

        if (!flashcard.getUser().getId().equals(user.getId())) {
            throw new ForbiddenException("You do not own this flashcard");
        }

        ReviewResult result = flashcardReviewService.review(flashcard.getReviewLevel(), correct);

        flashcard.setReviewLevel(result.reviewLevel());
        flashcard.setNextReviewDate(result.nextReviewDate());

        return flashcardRepository.save(flashcard);
    }
}
