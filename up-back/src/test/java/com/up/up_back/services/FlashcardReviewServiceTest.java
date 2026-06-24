package com.up.up_back.services;

import com.up.up_back.domain.ReviewResult;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FlashcardReviewServiceTest {

    private final FlashcardReviewService flashcardReviewService = new FlashcardReviewServiceImpl();

    @Test
    void shouldIncreaseReviewLevelWhenAnswerIsCorrect() {

        ReviewResult result = flashcardReviewService.review(0, true);

        assertEquals(1, result.reviewLevel());
    }

    @Test
    void shouldResetReviewLevelWhenAnswerIsWrong() {

        ReviewResult result = flashcardReviewService.review(3, false);

        assertEquals(0, result.reviewLevel());
    }

    @Test
    void shouldScheduleReviewForThreeDaysWhenLevelBecomesOne() {

        ReviewResult result = flashcardReviewService.review(0, true);

        assertEquals(LocalDate.now().plusDays(3), result.nextReviewDate());
    }

    @Test
    void shouldScheduleReviewForOneDayWhenAnswerIsWrong() {

        ReviewResult result = flashcardReviewService.review(2, false);

        assertEquals(LocalDate.now().plusDays(1), result.nextReviewDate());
    }

    @Test
    void shouldNotIncreaseLevelAboveMaximum() {

        ReviewResult result = flashcardReviewService.review(3, true);

        assertEquals(3, result.reviewLevel());
    }
}
