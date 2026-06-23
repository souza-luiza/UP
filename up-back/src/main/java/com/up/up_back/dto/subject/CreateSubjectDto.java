package com.up.up_back.dto.subject;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateSubjectDto(

        @NotBlank
        String name,

        @Min(1)
        @Max(5)
        Integer difficulty

) {
}
