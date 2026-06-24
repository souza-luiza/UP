package com.up.up_back.repository;

import com.up.up_back.entity.Flashcard;
import com.up.up_back.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

    List<Flashcard> findByUser(User user);

    List<Flashcard> findByUserAndNextReviewDateLessThanEqual(User user, LocalDate date);
}
