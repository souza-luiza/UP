package com.up.up_back.domain;

import java.time.LocalDate;

public record ReviewResult(
        int reviewLevel,
        LocalDate nextReviewDate
) {
}
