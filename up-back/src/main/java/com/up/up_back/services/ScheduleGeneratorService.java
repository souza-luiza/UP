package com.up.up_back.services;

import com.up.up_back.domain.Availability;
import com.up.up_back.domain.ClassSession;
import com.up.up_back.domain.StudySession;
import com.up.up_back.domain.Subject;

import java.util.List;

public interface ScheduleGeneratorService {

    List<StudySession> generate(List<Subject> subjects, List<Availability> availabilities, List<ClassSession> classes);
}
