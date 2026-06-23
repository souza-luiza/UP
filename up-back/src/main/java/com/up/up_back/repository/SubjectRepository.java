package com.up.up_back.repository;

import com.up.up_back.entity.Subject;
import com.up.up_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {

    List<Subject> findByUser(User user);
}
