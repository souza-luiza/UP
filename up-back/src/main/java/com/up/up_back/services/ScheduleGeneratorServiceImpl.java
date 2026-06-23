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

    @Override
    public List<StudySession> generate(List<Subject> subjects, List<Availability> availabilities, List<ClassSession> classes) {
        List<StudySession> sessions = new ArrayList<>();

        Availability availability = availabilities.getFirst();

        long availableMinutes = Duration.between(availability.start(), availability.end()).toMinutes();

        int totalDifficulty = subjects.stream()
                .mapToInt(Subject::difficulty)
                .sum();

        long currentMinute = availability.start().toSecondOfDay() / 60;

        for(Subject subject : subjects) {

            long subjectMinutes = availableMinutes * subject.difficulty() / totalDifficulty;

            StudySession session = StudySession.builder()
                    .subjectName(subject.name())
                    .dayOfWeek(availability.dayOfWeek())
                    .start(LocalTime.of(
                            (int) currentMinute / 60,
                            (int) currentMinute % 60
                    ))
                    .end(LocalTime.of(
                            (int) (currentMinute + subjectMinutes) / 60,
                            (int) (currentMinute + subjectMinutes) % 60
                    ))
                    .build();

            sessions.add(session);

            currentMinute += subjectMinutes;
        }

        return sessions;
    }
}
