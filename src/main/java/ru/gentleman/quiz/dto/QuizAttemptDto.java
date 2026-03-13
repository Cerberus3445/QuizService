package ru.gentleman.quiz.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record QuizAttemptDto(
        UUID id,
        UUID userId,
        UUID quizId,
        LocalDateTime createdAt,
        LocalDateTime completedAt,
        Boolean isActive
) {
}
