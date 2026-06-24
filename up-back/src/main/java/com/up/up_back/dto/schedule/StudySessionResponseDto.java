package com.up.up_back.dto.schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record StudySessionResponseDto(
        String subjectName,
        DayOfWeek dayOfWeek,
        LocalTime start,
        LocalTime end
) {
}
