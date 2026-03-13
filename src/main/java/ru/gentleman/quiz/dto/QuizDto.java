package ru.gentleman.quiz.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record QuizDto(
        UUID id,
        UUID lessonId,
        String title,
        String description,
        Boolean isActive
) {
}
