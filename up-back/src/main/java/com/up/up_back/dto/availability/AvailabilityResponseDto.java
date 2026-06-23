package com.up.up_back.dto.availability;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record AvailabilityResponseDto(
        Long id,
        DayOfWeek dayOfWeek,
        LocalTime startTime,
        LocalTime endTime
) {
}
