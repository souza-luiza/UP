package com.up.up_back.services;

import com.up.up_back.domain.ReviewResult;

public interface FlashcardReviewService {

    ReviewResult review(int currentLevel, boolean correct);
}
