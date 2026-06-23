package com.up.up_back.services;

import com.up.up_back.domain.Availability;
import com.up.up_back.domain.ClassSession;
import com.up.up_back.domain.StudySession;
import com.up.up_back.domain.Subject;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScheduleGeneratorServiceImpl implements ScheduleGeneratorService {

    private static final long MAX_SESSION_MINUTES = 120;

    @Override
    public List<StudySession> generate(List<Subject> subjects, List<Availability> availabilities, List<ClassSession> classes) {
        List<StudySession> sessions = new ArrayList<>();

        Availability availability = availabilities.getFirst();

        long availableMinutes = Duration.between(availability.start(), availability.end()).toMinutes();

        int totalDifficulty = subjects.stream()
                .mapToInt(Subject::difficulty)
                .sum();

        long currentMinute = availability.start().toSecondOfDay() / 60;

        long allocatedMinutes = 0; // Mudanca minima para TDD

        for (int i = 0; i < subjects.size(); i++) {
            Subject subject = subjects.get(i);

            long subjectMinutes = availableMinutes * subject.difficulty() / totalDifficulty;

            boolean isLastSubject = i == subjects.size() - 1; // Mudanca minima para TDD

            if (isLastSubject) subjectMinutes = availableMinutes - allocatedMinutes; // Mudanca minima para TDD

            long remainingMinutes = subjectMinutes;

            while(remainingMinutes > 0) {

                long sessionMinutes = Math.min(remainingMinutes, MAX_SESSION_MINUTES);

                StudySession session = StudySession.builder()
                        .subjectName(subject.name())
                        .dayOfWeek(availability.dayOfWeek())
                        .start(LocalTime.of(
                                (int) currentMinute / 60,
                                (int) currentMinute % 60
                        ))
                        .end(LocalTime.of(
                                (int) (currentMinute + sessionMinutes) / 60,
                                (int) (currentMinute + sessionMinutes) % 60
                        ))
                        .build();

                sessions.add(session);

                currentMinute += sessionMinutes;

                remainingMinutes -= sessionMinutes;
            }

            allocatedMinutes += subjectMinutes; // Mudanca minima para TDD
        }

        return sessions;
    }
}
