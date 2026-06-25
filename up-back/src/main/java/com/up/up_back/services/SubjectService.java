package com.up.up_back.services;

import com.up.up_back.dto.subject.CreateSubjectDto;
import com.up.up_back.entity.Subject;
import com.up.up_back.entity.User;

import java.util.List;

public interface SubjectService {

    Subject create(CreateSubjectDto dto, User user);

    List<Subject> findAll(User user);
}
