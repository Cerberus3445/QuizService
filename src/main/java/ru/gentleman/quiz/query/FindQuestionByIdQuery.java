package ru.gentleman.quiz.query;

import java.util.UUID;

public record FindQuestionByIdQuery(
        UUID id
) {
}
