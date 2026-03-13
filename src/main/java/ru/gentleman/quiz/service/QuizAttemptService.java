package ru.gentleman.quiz.service;

import ru.gentleman.quiz.dto.QuizAttemptDto;
import ru.gentleman.quiz.entity.QuizAttempt;

import java.util.UUID;

public interface QuizAttemptService  {

    void create(QuizAttemptDto dto);

    QuizAttemptDto getByUserIdAndQuizId(UUID userId, UUID quizId);

    QuizAttemptDto finishQuizAttempt(UUID id);
}
