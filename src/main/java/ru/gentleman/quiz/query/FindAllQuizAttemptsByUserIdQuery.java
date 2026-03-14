package ru.gentleman.quiz.query;

import java.util.UUID;

public record FindAllQuizAttemptsByUserIdQuery(
        UUID userId
) {
}
