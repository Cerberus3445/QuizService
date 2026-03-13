package ru.gentleman.quiz.service;

import ru.gentleman.common.service.CrudOperations;
import ru.gentleman.quiz.dto.QuestionDto;

import java.util.UUID;

public interface QuestionService extends CrudOperations<QuestionDto, UUID> {

}
