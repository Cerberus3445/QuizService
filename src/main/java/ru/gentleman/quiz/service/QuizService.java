package ru.gentleman.quiz.service;

import ru.gentleman.common.service.CrudOperations;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.dto.QuizDto;

import java.util.List;
import java.util.UUID;

public interface QuizService extends CrudOperations<QuizDto, UUID>{

    List<QuizDto> getAllByLessonId(UUID id);

    boolean existsById(UUID id);

    List<QuestionDto> getAllQuestions(UUID id);

    UUID getQuizIdByQuestionId(UUID questionId);
}
