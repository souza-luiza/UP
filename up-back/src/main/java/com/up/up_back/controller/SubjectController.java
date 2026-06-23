package com.up.up_back.controller;

import com.up.up_back.dto.subject.CreateSubjectDto;
import com.up.up_back.dto.subject.SubjectResponseDto;
import com.up.up_back.entity.Subject;
import com.up.up_back.security.UserDetailsImpl;
import com.up.up_back.services.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    public SubjectResponseDto create(@RequestBody @Valid CreateSubjectDto dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {

        Subject subject = subjectService.create(dto, userDetails.getUser());

        return new SubjectResponseDto(subject.getId(), subject.getName(), subject.getDifficulty());
    }
}
