package ru.gentleman.quiz.dto;

import java.util.UUID;

public record QuestionDto(
        UUID id,
        UUID quizId,

        String title,

        String correctAnswer,

        String explanation

) {
}
