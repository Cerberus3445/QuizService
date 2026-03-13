package ru.gentleman.quiz.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import java.util.UUID;

public record DeleteQuizCommand(
        @TargetAggregateIdentifier
        UUID id
) {
}
