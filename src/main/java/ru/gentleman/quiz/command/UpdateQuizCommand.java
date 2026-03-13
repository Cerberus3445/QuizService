package ru.gentleman.quiz.command;

import lombok.Builder;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

@Builder
public record UpdateQuizCommand(
        @TargetAggregateIdentifier
        UUID id,
        UUID lessonId,
        String title,
        String description
) {
}
