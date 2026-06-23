package com.up.up_back.services;

import com.up.up_back.domain.Availability;
import com.up.up_back.domain.StudySession;
import com.up.up_back.domain.Subject;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void shouldAllocateAllAvailableTime() {

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

        long totalMinutes = sessions.stream()
                .mapToLong(session -> Duration.between(session.start(), session.end()).toMinutes())
                .sum();

        assertEquals(360, totalMinutes);
    }

    @Test
    void shouldAllocateAllAvailableTimeEvenWhenDivisionProducesRemainder() {

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
                .end(LocalTime.of(20, 5))
                .build();

        List<StudySession> sessions = scheduleGeneratorService.generate(
                List.of(poo, bd),
                List.of(availability),
                List.of()
        );

        long totalMinutes = sessions.stream()
                .mapToLong(session -> Duration.between(session.start(), session.end()).toMinutes())
                .sum();

        assertEquals(365, totalMinutes);
    }

    @Test
    void shouldRespectMaximumSessionDurationOfTwoHours() {

        Subject bd = Subject.builder()
                .name("Banco de Dados")
                .difficulty(5)
                .build();

        Availability availability = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(14, 0))
                .end(LocalTime.of(20, 5))
                .build();

        List<StudySession> sessions = scheduleGeneratorService.generate(
                List.of(bd),
                List.of(availability),
                List.of()
        );

        boolean allSessionsWithinLimit = sessions.stream()
                .allMatch(session -> Duration.between(session.start(), session.end()).toMinutes() <= 120);

        assertTrue(allSessionsWithinLimit);
    }

    @Test
    void shouldCreateMoreThanOneSessionWhenAllocatedTimeExceedsTwoHours() {

        Subject bd = Subject.builder()
                .name("Banco de Dados")
                .difficulty(5)
                .build();

        Availability availability = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(14, 0))
                .end(LocalTime.of(20, 5))
                .build();

        List<StudySession> sessions = scheduleGeneratorService.generate(
                List.of(bd),
                List.of(availability),
                List.of()
        );

        assertTrue(sessions.size() > 1);
    }

    @Test
    void shouldUseAllAvailabilities() {

        Subject bd = Subject.builder()
                .name("Banco de Dados")
                .difficulty(5)
                .build();

        Availability monday = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(19, 0))
                .end(LocalTime.of(21, 0))
                .build();

        Availability wednesday = Availability.builder()
                .dayOfWeek(DayOfWeek.WEDNESDAY)
                .start(LocalTime.of(19, 0))
                .end(LocalTime.of(21, 0))
                .build();

        Availability friday = Availability.builder()
                .dayOfWeek(DayOfWeek.FRIDAY)
                .start(LocalTime.of(19, 0))
                .end(LocalTime.of(21, 0))
                .build();

        List<StudySession> sessions = scheduleGeneratorService.generate(
                List.of(bd),
                List.of(monday, wednesday, friday),
                List.of()
        );

        long totalMinutes = sessions.stream()
                .mapToLong(session -> Duration.between(session.start(), session.end()).toMinutes())
                .sum();

        assertEquals(360, totalMinutes);
    }

    @Test
    void shouldGenerateSessionsAcrossMultipleDays() {

        Subject bd = Subject.builder()
                .name("Banco de Dados")
                .difficulty(5)
                .build();

        Availability monday = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(19, 0))
                .end(LocalTime.of(21, 0))
                .build();

        Availability wednesday = Availability.builder()
                .dayOfWeek(DayOfWeek.WEDNESDAY)
                .start(LocalTime.of(19, 0))
                .end(LocalTime.of(21, 0))
                .build();

        Availability friday = Availability.builder()
                .dayOfWeek(DayOfWeek.FRIDAY)
                .start(LocalTime.of(19, 0))
                .end(LocalTime.of(21, 0))
                .build();

        List<StudySession> sessions = scheduleGeneratorService.generate(
                List.of(bd),
                List.of(monday, wednesday, friday),
                List.of()
        );

        assertTrue(sessions.stream().anyMatch(s -> s.dayOfWeek() == DayOfWeek.MONDAY));
        assertTrue(sessions.stream().anyMatch(s -> s.dayOfWeek() == DayOfWeek.WEDNESDAY));
        assertTrue(sessions.stream().anyMatch(s -> s.dayOfWeek() == DayOfWeek.FRIDAY));
    }

    @Test
    void shouldReturnEmptyScheduleWhenThereIsNoAvailability() {

        Subject bd = Subject.builder()
                .name("Banco de Dados")
                .difficulty(5)
                .build();

        List<StudySession> sessions = scheduleGeneratorService.generate(
                List.of(bd),
                List.of(),
                List.of()
        );

        assertTrue(sessions.isEmpty());
    }
}
