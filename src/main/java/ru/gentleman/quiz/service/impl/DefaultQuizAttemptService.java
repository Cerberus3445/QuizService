package ru.gentleman.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.quiz.dto.QuizAttemptDto;
import ru.gentleman.quiz.entity.Quiz;
import ru.gentleman.quiz.entity.QuizAttempt;
import ru.gentleman.quiz.entity.UserAnswer;
import ru.gentleman.quiz.mapper.QuizAttemptMapper;
import ru.gentleman.quiz.repository.QuizAttemptRepository;
import ru.gentleman.quiz.service.QuizAttemptService;
import ru.gentleman.quiz.service.QuizService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DefaultQuizAttemptService implements QuizAttemptService {

    private final QuizAttemptRepository quizAttemptRepository;

    private final QuizAttemptMapper quizAttemptMapper;

    private final QuizService quizService;

    @Override
    @Transactional
    public void create(QuizAttemptDto dto) {
        log.info("create {}", dto);

        this.quizService.get(dto.quizId());

        this.quizAttemptRepository.save(this.quizAttemptMapper.toEntity(dto));
    }

    @Override
    public Boolean existsById(UUID id) {
        log.info("existsById {}", id);

        return this.quizAttemptRepository.existsById(id);
    }

    @Override
    public QuizAttemptDto getByUserIdAndQuizId(UUID userId, UUID quizId) {
        log.info("getByUserIdAndQuizId {}, {}", userId, quizId);

        QuizAttempt quizAttempt =
                this.quizAttemptRepository.findByUserIdAndQuiz_Id(userId, quizId).orElse(null);

        return this.quizAttemptMapper.toDto(quizAttempt);
    }

    @Override
    @Transactional //TODO перенести в агрегат
    public QuizAttemptDto finishQuizAttempt(UUID id) {
        log.info("finishQuizAttempt {}", id);

        QuizAttempt quizAttempt = this.quizAttemptRepository.findById(id)
                //TODO мб надо в runtime прокидывать исключения из write side
                .orElseThrow(() -> ExceptionUtils.notFound("error.quiz_attempt.not_found_id",id));
        Quiz quiz = quizAttempt.getQuiz();
        List<UserAnswer> userAnswers = quizAttempt.getUserAnswers();

        if(quiz.getQuestions().size() != userAnswers.size()) {
            throw new RuntimeException("Не все вопросы теста решены");
        }

        for(UserAnswer userAnswer : userAnswers) {
            if(userAnswer.getIsCorrect()) {

            }
        }

        return null;
    }
}
