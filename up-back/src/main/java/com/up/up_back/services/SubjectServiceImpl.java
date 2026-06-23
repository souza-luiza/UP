package com.up.up_back.services;

import com.up.up_back.dto.subject.CreateSubjectDto;
import com.up.up_back.entity.Subject;
import com.up.up_back.entity.User;
import com.up.up_back.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubjectServiceImpl implements SubjectService {

    private final SubjectRepository subjectRepository;

    @Override
    public Subject create(CreateSubjectDto dto, User user) {

        Subject subject = Subject.builder()
                .name(dto.name())
                .difficulty(dto.difficulty())
                .user(user)
                .build();

        return subjectRepository.save(subject);
    }

}
