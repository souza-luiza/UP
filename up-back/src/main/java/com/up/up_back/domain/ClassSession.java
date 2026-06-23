package com.up.up_back.domain;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ClassSession(

        String subjectName,

        DayOfWeek dayOfWeek,

        LocalTime start,

        LocalTime end
) {
}
