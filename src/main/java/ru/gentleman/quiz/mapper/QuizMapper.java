package ru.gentleman.quiz.mapper;

import org.mapstruct.Mapper;
import ru.gentleman.quiz.dto.QuizDto;
import ru.gentleman.quiz.entity.Quiz;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    Quiz toEntity(QuizDto dto);

    QuizDto toDto(Quiz entity);

    List<QuizDto> toDto(List<Quiz> entities);
}
