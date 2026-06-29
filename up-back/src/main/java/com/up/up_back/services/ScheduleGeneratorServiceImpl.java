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

    private static final long MINIMUM_MINUTES_PER_SUBJECT = 30;

    @Override
    public List<StudySession> generate(List<Subject> subjects, List<Availability> availabilities, List<ClassSession> classes) {
        List<StudySession> sessions = new ArrayList<>();

        long availableMinutes = calculateAvailableMinutes(availabilities);

        long minimumRequiredMinutes = (long) subjects.size() * MINIMUM_MINUTES_PER_SUBJECT;

        if (availableMinutes < minimumRequiredMinutes) {
            throw new IllegalArgumentException("Not enough available time to generate schedule");
        }

        int totalDifficulty = subjects.stream()
                .mapToInt(Subject::difficulty)
                .sum();

        List<Long> minutesPerSubject = new ArrayList<>();

        long allocatedMinutes = 0;

        for (int i = 0; i < subjects.size(); i++) {

            long subjectMinutes = availableMinutes * subjects.get(i).difficulty() / totalDifficulty;

            if (i == subjects.size() - 1) subjectMinutes = availableMinutes - allocatedMinutes;

            allocatedMinutes += subjectMinutes;
            minutesPerSubject.add(subjectMinutes);
        }

        allocateSessions(subjects, minutesPerSubject, availabilities, sessions);

        return sessions;
    }

    private long calculateAvailableMinutes( List<Availability> availabilities ) {

        return availabilities.stream()
                .mapToLong(
                        availability ->
                                Duration.between(availability.start(), availability.end()).toMinutes()
                )
                .sum();
    }

    private void allocateSessions(List<Subject> subjects, List<Long> minutesPerSubject, List<Availability> availabilities, List<StudySession> sessions) {

        int subjectIndex = 0;
        long remainingSubjectMinutes = minutesPerSubject.getFirst();

        for(Availability availability : availabilities) {

            LocalTime current = availability.start();

            long availabilityMinutes = Duration.between(availability.start(), availability.end()).toMinutes();

            while (availabilityMinutes > 0 && subjectIndex < subjects.size()) {

                long sessionMinutes = Math.min(Math.min(availabilityMinutes, remainingSubjectMinutes), MAX_SESSION_MINUTES);

                StudySession session = StudySession.builder()
                        .subjectName(subjects.get(subjectIndex).name())
                        .dayOfWeek(availability.dayOfWeek())
                        .start(current)
                        .end(current.plusMinutes(sessionMinutes))
                        .build();

                sessions.add(session);

                current = current.plusMinutes(sessionMinutes);

                availabilityMinutes -= sessionMinutes;

                remainingSubjectMinutes -= sessionMinutes;

                if (remainingSubjectMinutes == 0) {
                    subjectIndex++;
                    if (subjectIndex < subjects.size()) remainingSubjectMinutes = minutesPerSubject.get(subjectIndex);
                }
            }
        }
    }
}