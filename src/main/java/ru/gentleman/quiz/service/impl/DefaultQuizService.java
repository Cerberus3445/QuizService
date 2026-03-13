package ru.gentleman.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.dto.QuizDto;
import ru.gentleman.quiz.entity.Quiz;
import ru.gentleman.quiz.mapper.QuestionMapper;
import ru.gentleman.quiz.mapper.QuizMapper;
import ru.gentleman.quiz.repository.QuizRepository;
import ru.gentleman.quiz.service.QuizService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultQuizService implements QuizService {

    private final QuizRepository quizRepository;

    private final QuizMapper quizMapper;

    private final QuestionMapper questionMapper;

    @Override
    @Cacheable(value = "quiz", key = "#id")
    public QuizDto get(UUID id) {
        log.info("get {}", id);

        Quiz quiz = this.quizRepository.findById(id).orElse(null);

        return this.quizMapper.toDto(quiz);
    }

    @Override
    @Transactional
    @CacheEvict(value = "quizzesByLessonId", key = "#quizDto.lessonId()")
    public QuizDto create(QuizDto quizDto) {
        log.info("create {}", quizDto);

        Quiz createdQuiz =
                this.quizRepository.save(this.quizMapper.toEntity(quizDto));

        return this.quizMapper.toDto(createdQuiz);
    }

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "quiz", key = "#id"),
            @CacheEvict(value = "quizzesByLessonId", key = "#quizDto.lessonId()")
    })
    public void update(UUID id, QuizDto quizDto) {
        log.info("update {}, {}", id, quizDto);

        this.quizRepository.findById(id).ifPresentOrElse(question -> {
            Quiz updatedQuiz = Quiz.builder()
                    .id(id)
                    .title(quizDto.title())
                    .description(quizDto.description())
                    .lessonId(quizDto.lessonId())
                    .build();

            this.quizRepository.save(updatedQuiz);
        }, () -> {
            throw ExceptionUtils.notFound("error.quiz.not_found_id", id);
        });
    }

    @Override
    @Transactional
    @Cacheable(value = "quiz", key = "#id")
    public void delete(UUID id) {
        log.info("delete {}", id);

        Quiz quiz = this.quizRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.notFound("error.quiz.not_found_id", id));

        this.quizRepository.delete(quiz);
    }

    @Override
    @Cacheable(value = "quizzesByLessonId", key = "#lessonId")
    public List<QuizDto> getAllByLessonId(UUID lessonId) {
        log.info("getAllByLessonId {}", lessonId);

        return this.quizMapper.toDto(
                this.quizRepository.findAllByLessonId(lessonId)
        );
    }

    @Override
    @Cacheable(value = "allQuestionByQuiz", key = "#id")
    public List<QuestionDto> getAllQuestions(UUID id) {
        log.info("getAllQuestions {}", id);

        Quiz quiz = this.quizRepository.findById(id)
                .orElse(null);

        if(quiz == null) {
            return null;
        }

        return this.questionMapper.toDto(quiz.getQuestions());
    }

    @Override
    public UUID getQuizIdByQuestionId(UUID questionId) {
        log.info("getQuizIdByQuestionId {}", questionId);

        return this.quizRepository.findByQuestionId(questionId)
                .orElseThrow(() -> ExceptionUtils.notFound("error.quiz.not_found_question_id", questionId));
    }
}
