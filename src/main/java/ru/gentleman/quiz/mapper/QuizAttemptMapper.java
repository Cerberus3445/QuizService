package ru.gentleman.quiz.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gentleman.quiz.dto.QuizAttemptDto;
import ru.gentleman.quiz.entity.QuizAttempt;

@Mapper(componentModel = "spring")
public interface QuizAttemptMapper {

    @Mapping(target = "quiz.id", source = "quizId")
    QuizAttempt toEntity(QuizAttemptDto dto);

    @Mapping(target = "quizId", source = "quiz.id")
    QuizAttemptDto toDto(QuizAttempt entity);
}
