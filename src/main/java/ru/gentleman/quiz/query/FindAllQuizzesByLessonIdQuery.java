package ru.gentleman.quiz.query;

import java.util.UUID;

public record FindAllQuizzesByLessonIdQuery(
        UUID lessonId
) {
}
