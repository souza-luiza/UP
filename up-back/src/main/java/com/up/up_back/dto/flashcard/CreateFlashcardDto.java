package com.up.up_back.dto.flashcard;

import jakarta.validation.constraints.NotBlank;

public record CreateFlashcardDto(

        @NotBlank
        String question,

        @NotBlank
        String answer

) {
}
