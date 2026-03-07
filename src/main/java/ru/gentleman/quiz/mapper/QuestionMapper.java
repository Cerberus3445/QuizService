package ru.gentleman.quiz.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.entity.Question;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "quiz.id", source = "quizId")
    Question toEntity(QuestionDto dto);

    @Mapping(target = "quizId", source = "quiz.id")
    QuestionDto toDto(Question entity);
}
