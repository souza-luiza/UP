package com.up.up_back.domain;

import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Builder
public record StudySession(

        String subjectName,

        DayOfWeek dayOfWeek,

        LocalTime start,

        LocalTime end
) {
}
