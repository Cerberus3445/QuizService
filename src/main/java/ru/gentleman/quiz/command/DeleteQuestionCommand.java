package ru.gentleman.quiz.command;

import org.axonframework.modelling.command.AggregateIdentifier;

import java.util.UUID;

public record DeleteQuestionCommand(
        UUID id,
        @AggregateIdentifier
        UUID quizId
) {
}
