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
    void shouldThrowExceptionWhenThereIsNoAvailability() {

        Subject bd = Subject.builder()
                .name("Banco de Dados")
                .difficulty(5)
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> scheduleGeneratorService.generate(
                        List.of(bd),
                        List.of(),
                        List.of()
                ));
    }

    @Test
    void shouldThrowExceptionWhenThereIsNotEnoughTimeForAllSubjects() {

        Subject bd = Subject.builder()
                .name("Banco de Dados")
                .difficulty(5)
                .build();

        Subject poo = Subject.builder()
                .name("POO")
                .difficulty(3)
                .build();

        Subject calculo = Subject.builder()
                .name("Calculo")
                .difficulty(4)
                .build();

        Availability availability = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(19, 0))
                .end(LocalTime.of(20, 0))
                .build();

        assertThrows(
                IllegalArgumentException.class,
                () -> scheduleGeneratorService.generate(
                        List.of(bd, poo, calculo),
                        List.of(availability),
                        List.of()
                ));
    }

    @Test
    void shouldNotGenerateOverlappingSessions() {
        Subject bd = Subject.builder()
                .name("BD")
                .difficulty(5)
                .build();

        Subject poo = Subject.builder()
                .name("POO")
                .difficulty(5)
                .build();

        Availability availability = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(19, 0))
                .end(LocalTime.of(23, 0))
                .build();

        List<StudySession> sessions = scheduleGeneratorService.generate(
                List.of(bd, poo),
                List.of(availability),
                List.of()
        );

        for (int i = 0; i < sessions.size() - 1; i++) {
            assertFalse(
                    sessions.get(i).end().isAfter(sessions.get(i + 1).start())
            );
        }
    }

    @Test
    void shouldGenerateScheduleWhenAvailableTimeIsExactlyMinimumRequired() {

        Subject bd = Subject.builder()
                .name("BD")
                .difficulty(5)
                .build();

        Subject poo = Subject.builder()
                .name("POO")
                .difficulty(5)
                .build();

        Availability availability = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(19, 0))
                .end(LocalTime.of(20, 0))
                .build();

        List<StudySession> sessions =
                scheduleGeneratorService.generate(
                        List.of(bd, poo),
                        List.of(availability),
                        List.of());

        assertFalse(sessions.isEmpty());

        long totalMinutes = sessions.stream()
                .mapToLong(s -> Duration.between(s.start(), s.end()).toMinutes())
                .sum();

        assertEquals(60, totalMinutes);
    }

    @Test
    void shouldDistributeMinutesProportionallyBetweenSubjects() {

        Subject easy = Subject.builder()
                .name("Easy")
                .difficulty(1)
                .build();

        Subject hard = Subject.builder()
                .name("Hard")
                .difficulty(3)
                .build();

        Availability availability = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(18, 0))
                .end(LocalTime.of(22, 0))
                .build();

        List<StudySession> sessions =
                scheduleGeneratorService.generate(
                        List.of(easy, hard),
                        List.of(availability),
                        List.of());

        long easyMinutes =
                sessions.stream()
                        .filter(s -> s.subjectName().equals("Easy"))
                        .mapToLong(s ->
                                Duration.between(s.start(), s.end()).toMinutes())
                        .sum();

        long hardMinutes =
                sessions.stream()
                        .filter(s -> s.subjectName().equals("Hard"))
                        .mapToLong(s ->
                                Duration.between(s.start(), s.end()).toMinutes())
                        .sum();

        assertEquals(60, easyMinutes);
        assertEquals(180, hardMinutes);
    }

    @Test
    void shouldAssignRemainingMinutesToLastSubject() {

        Subject s1 = Subject.builder()
                .name("S1")
                .difficulty(1)
                .build();

        Subject s2 = Subject.builder()
                .name("S2")
                .difficulty(1)
                .build();

        Subject s3 = Subject.builder()
                .name("S3")
                .difficulty(1)
                .build();

        Availability availability = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(18,0))
                .end(LocalTime.of(19,40))
                .build();

        List<StudySession> sessions =
                scheduleGeneratorService.generate(
                        List.of(s1,s2,s3),
                        List.of(availability),
                        List.of());

        long s1Minutes = sessions.stream()
                .filter(s -> s.subjectName().equals("S1"))
                .mapToLong(s -> Duration.between(s.start(), s.end()).toMinutes())
                .sum();

        long s2Minutes = sessions.stream()
                .filter(s -> s.subjectName().equals("S2"))
                .mapToLong(s -> Duration.between(s.start(), s.end()).toMinutes())
                .sum();

        long s3Minutes = sessions.stream()
                .filter(s -> s.subjectName().equals("S3"))
                .mapToLong(s -> Duration.between(s.start(), s.end()).toMinutes())
                .sum();

        assertEquals(33, s1Minutes);
        assertEquals(33, s2Minutes);
        assertEquals(34, s3Minutes);
    }

    @Test
    void shouldStopGeneratingSessionsWhenAvailabilityEndsExactly() {

        Subject bd = Subject.builder()
                .name("BD")
                .difficulty(5)
                .build();

        Availability availability = Availability.builder()
                .dayOfWeek(DayOfWeek.MONDAY)
                .start(LocalTime.of(18,0))
                .end(LocalTime.of(19,0))
                .build();

        List<StudySession> sessions =
                scheduleGeneratorService.generate(
                        List.of(bd),
                        List.of(availability),
                        List.of());

        assertEquals(1, sessions.size());

        assertEquals(LocalTime.of(18,0), sessions.getFirst().start());
        assertEquals(LocalTime.of(19,0), sessions.getFirst().end());
    }
}
