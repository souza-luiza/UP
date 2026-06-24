package com.up.up_back.dto.flashcard;

import java.time.LocalDate;

public record FlashcardResponseDto(

        Long id,

        String question,

        String answer,

        Integer reviewLevel,

        LocalDate nextReviewDate

) {
}
