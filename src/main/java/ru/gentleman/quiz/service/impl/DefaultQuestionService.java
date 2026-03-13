package ru.gentleman.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.entity.Question;
import ru.gentleman.quiz.mapper.QuestionMapper;
import ru.gentleman.quiz.repository.QuestionRepository;
import ru.gentleman.quiz.service.QuestionService;
import ru.gentleman.quiz.service.QuizService;

import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor //TODO добавить мягкое удаление
public class DefaultQuestionService implements QuestionService {

    private final QuestionRepository questionRepository;

    private final QuestionMapper questionMapper;

    private final QuizService quizService;

    @Override
    @Cacheable(value = "question", key = "#id")
    public QuestionDto get(UUID id) {
        log.info("get {}", id);

        Question question = this.questionRepository.findById(id)
                .orElse(null);

        return this.questionMapper.toDto(question);
    }

    @Override
    @Transactional
    public QuestionDto create(QuestionDto questionDto) {
        log.info("create {}", questionDto);

        this.quizService.get(questionDto.quizId());

        Question createdQuestion =
                this.questionRepository.save(this.questionMapper.toEntity(questionDto));

        return this.questionMapper.toDto(createdQuestion);
    }

    @Override
    @Transactional
    @Cacheable(value = "question", key = "#id")
    public void update(UUID id, QuestionDto questionDto) {
        log.info("update {}, {}", id, questionDto);

        this.questionRepository.findById(id).ifPresentOrElse(question -> {
            Question updatedQuestion = Question.builder()
                    .id(id) //TODO
                    .title(questionDto.title())
                    .description(questionDto.description())
                    .correctAnswer(questionDto.correctAnswer())
                    .explanation(questionDto.explanation())
                    .quiz(question.getQuiz())
                    .build();

            this.questionRepository.save(updatedQuestion);
        }, () -> {
            throw ExceptionUtils.notFound("error.question.not_found_id", id);
        });
    }

    @Override
    @Transactional
    @Cacheable(value = "question", key = "#id")
    public void delete(UUID id) {
        log.info("delete {}", id);

        Question question = this.questionRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.question.not_found_id", id));

        this.questionRepository.delete(question);
    }
}
