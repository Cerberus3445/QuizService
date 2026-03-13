package ru.gentleman.quiz.command;

import lombok.Builder;
import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.UUID;

@Builder
public record UpdateQuestionCommand(
        UUID id,
        @AggregateIdentifier
        UUID quizId,
        String title,
        String description,
        String correctAnswer,
        String explanation,
        int score
) {
}
