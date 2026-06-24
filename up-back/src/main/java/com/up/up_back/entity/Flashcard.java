package com.up.up_back.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "flashcards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flashcard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String answer;

    @Builder.Default
    private Integer reviewLevel = 0;

    private LocalDate nextReviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
