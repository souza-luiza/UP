package com.up.up_back.services;

import com.up.up_back.domain.Availability;
import com.up.up_back.domain.StudySession;
import com.up.up_back.domain.Subject;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ScheduleGeneratorServiceTest {

    private final ScheduleGeneratorService scheduleGeneratorService = new ScheduleGeneratorServiceImpl();

    @Test
    void shouldAllocateMoreStudyTimeToMoreDifficultSubjects() {

        Subject poo = Subject.builder()
                .name("POO")
                .difficulty(1)
                .build();

        Subject bd = Subject.builder()
                .name("Banco de Dados")
                .difficulty(5)
                .build();

        Availability availability = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(14, 0))
                .end(LocalTime.of(20, 0))
                .build();

        List<StudySession> sessions = scheduleGeneratorService.generate(
                List.of(poo, bd),
                List.of(availability),
                List.of()
        );

        long bdMinutes = sessions.stream()
                .filter(s -> s.subjectName().equals("Banco de Dados"))
                .mapToLong(s -> Duration.between(s.start(), s.end()).toMinutes())
                .sum();

        long pooMinutes = sessions.stream()
                .filter(s -> s.subjectName().equals("POO"))
                .mapToLong(s -> Duration.between(s.start(), s.end()).toMinutes())
                .sum();

        assertTrue(bdMinutes > pooMinutes);
    }
}
