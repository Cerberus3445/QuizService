package ru.gentleman.quiz.command;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record CreateQuizAttemptCommand(
        @TargetAggregateIdentifier
        UUID id,
        UUID userId,
        UUID quizId,
        LocalDateTime createdAt
) {
}
