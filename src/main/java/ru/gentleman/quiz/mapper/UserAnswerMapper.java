package ru.gentleman.quiz.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gentleman.quiz.dto.UserAnswerDto;
import ru.gentleman.quiz.entity.UserAnswer;

@Mapper(componentModel = "spring")
public interface UserAnswerMapper {

    @Mapping(target = "question.id", source = "questionId")
    @Mapping(target = "quizAttempt.id", source = "quizAttemptId")
    UserAnswer toEntity(UserAnswerDto dto);

    @Mapping(target = "questionId", source = "question.id")
    @Mapping(target = "quizAttemptId", source = "quizAttempt.id")
    UserAnswerDto toDto(UserAnswer entity);
}
