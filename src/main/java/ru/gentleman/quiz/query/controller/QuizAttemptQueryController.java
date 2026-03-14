package ru.gentleman.quiz.query.controller;

import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import ru.gentleman.common.exception.NotFoundException;
import ru.gentleman.quiz.dto.QuizAttemptDto;
import ru.gentleman.quiz.query.FindAllQuestionsByQuizIdQuery;
import ru.gentleman.quiz.query.FindQuizAttemptByIdQuery;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quiz-attempts")
@RequiredArgsConstructor
public class QuizAttemptQueryController {

    private final QueryGateway queryGateway;

    @GetMapping("/{id}")
    public QuizAttemptDto get(@PathVariable("id") UUID id) {
        QuizAttemptDto quizAttemptDto = this.queryGateway.query(new FindQuizAttemptByIdQuery(id),
                ResponseTypes.instanceOf(QuizAttemptDto.class)).join();

        if(quizAttemptDto == null) {
            throw new NotFoundException("error.quiz_attempt.not_found_id", id);
        }

        return quizAttemptDto;
    }

    @GetMapping(params = "userId")
    public List<QuizAttemptDto> getAllByUserId(@RequestParam("userId")UUID userId) {
        return this.queryGateway.query(new FindAllQuestionsByQuizIdQuery(userId),
                ResponseTypes.multipleInstancesOf(QuizAttemptDto.class)).join();
    }
}
