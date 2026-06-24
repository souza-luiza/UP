package com.up.up_back.services;

import com.up.up_back.dto.flashcard.CreateFlashcardDto;
import com.up.up_back.entity.Flashcard;
import com.up.up_back.entity.User;

public interface FlashcardService {

    Flashcard create(CreateFlashcardDto dto, User user);
}
