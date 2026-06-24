package com.up.up_back.services;

import com.up.up_back.domain.ReviewResult;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class FlashcardReviewServiceImpl implements FlashcardReviewService {

    @Override
    public ReviewResult review(int currentLevel, boolean correct) {

        int newLevel;

        if(correct) newLevel = Math.min(currentLevel + 1, 3);
        else newLevel = 0;

        LocalDate nextReviewDate = LocalDate.now().plusDays(getIntervalDays(newLevel));

        return new ReviewResult(newLevel, nextReviewDate);
    }

    private long getIntervalDays(int level) {
        return switch (level) {
            case 0 -> 1;
            case 1 -> 3;
            case 2 -> 7;
            case 3 -> 15;
            default -> throw new IllegalArgumentException("Invalid review level");
        };
    }
}
