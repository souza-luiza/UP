package com.up.up_back.services;

import com.up.up_back.dto.flashcard.CreateFlashcardDto;
import com.up.up_back.entity.Flashcard;
import com.up.up_back.entity.User;

import java.util.List;

public interface FlashcardService {

    Flashcard create(CreateFlashcardDto dto, User user);

    List<Flashcard> findAllByUser(User user);

    List<Flashcard> findCardsDueForReview(User user);
}
