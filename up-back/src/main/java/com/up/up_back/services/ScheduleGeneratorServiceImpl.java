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

        long availableMinutes = calculateAvailableMinutes(availabilities);

        int totalDifficulty = subjects.stream()
                .mapToInt(Subject::difficulty)
                .sum();

        long allocatedMinutes = 0;

        for (int i = 0; i < subjects.size(); i++) {

            Subject subject = subjects.get(i);

            long subjectMinutes = availableMinutes * subject.difficulty() / totalDifficulty;

            boolean isLastSubject = i == subjects.size() - 1;

            if (isLastSubject) subjectMinutes = availableMinutes - allocatedMinutes;

            allocateSubject(subject, subjectMinutes, availabilities, sessions);

            allocatedMinutes += subjectMinutes;
        }

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

    private void allocateSubject(Subject subject, long subjectMinutes, List<Availability> availabilities, List<StudySession> sessions) {

        long remainingMinutes = subjectMinutes;

        for(Availability availability : availabilities) {

            if(remainingMinutes <= 0) break;

            long availabilityMinutes = Duration.between(availability.start(), availability.end()).toMinutes();

            long minutesToAllocate = Math.min(remainingMinutes, availabilityMinutes);

            long currentMinute = availability.start().toSecondOfDay() / 60;

            long remainingAvailabilityMinutes = minutesToAllocate;

            while (remainingAvailabilityMinutes > 0) {

                long sessionMinutes = Math.min(remainingAvailabilityMinutes, MAX_SESSION_MINUTES);

                StudySession session = StudySession.builder()
                        .subjectName(subject.name())
                        .dayOfWeek(availability.dayOfWeek())
                        .start(LocalTime.of((int) currentMinute / 60, (int) currentMinute % 60))
                        .end(LocalTime.of((int) (currentMinute + sessionMinutes) / 60, (int) (currentMinute + sessionMinutes) % 60))
                        .build();

                sessions.add(session);

                currentMinute += sessionMinutes;

                remainingAvailabilityMinutes -= sessionMinutes;
            }

            remainingMinutes -= minutesToAllocate;
        }
    }
}
