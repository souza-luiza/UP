package com.up.up_back.controller;

import com.up.up_back.domain.StudySession;
import com.up.up_back.dto.schedule.GeneratedScheduleResponseDto;
import com.up.up_back.dto.schedule.StudySessionResponseDto;
import com.up.up_back.entity.Availability;
import com.up.up_back.entity.Subject;
import com.up.up_back.entity.User;
import com.up.up_back.repository.AvailabilityRepository;
import com.up.up_back.repository.SubjectRepository;
import com.up.up_back.security.UserDetailsImpl;
import com.up.up_back.services.ScheduleGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final SubjectRepository subjectRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ScheduleGeneratorService scheduleGeneratorService;

    @PostMapping("/generate")
    public GeneratedScheduleResponseDto generate(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();

        List<Subject> subjects = subjectRepository.findByUser(user);

        List<Availability> availabilities = availabilityRepository.findByUser(user);

        List<com.up.up_back.domain.Subject> domainSubjects =
                subjects.stream()
                        .map(subject ->
                                com.up.up_back.domain.Subject.builder()
                                        .name(subject.getName())
                                        .difficulty(subject.getDifficulty())
                                        .build()
                        )
                        .toList();

        List<com.up.up_back.domain.Availability> domainAvailabilities =
                availabilities.stream()
                        .map(availability ->
                                com.up.up_back.domain.Availability.builder()
                                        .dayOfWeek(availability.getDayOfWeek())
                                        .start(availability.getStartTime())
                                        .end(availability.getEndTime())
                                        .build()
                        )
                        .toList();

        List<StudySession> sessions = scheduleGeneratorService.generate(domainSubjects, domainAvailabilities, List.of());

        List<StudySessionResponseDto> responseSessions =
                sessions.stream()
                        .map(session ->
                                new StudySessionResponseDto(
                                        session.subjectName(),
                                        session.dayOfWeek(),
                                        session.start(),
                                        session.end()
                                )
                        )
                        .toList();

        return new GeneratedScheduleResponseDto(responseSessions);
    }
}
