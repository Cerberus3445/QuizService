package ru.gentleman.quiz.query.controller;

import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.dto.QuizDto;
import ru.gentleman.quiz.query.FindAllQuestionsByQuizIdQuery;
import ru.gentleman.quiz.query.FindAllQuizzesByLessonIdQuery;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizQueryController {

    private final QueryGateway queryGateway;

    @GetMapping(params = "lessonId")
    public List<QuizDto> getAllByLessonId(@RequestParam("lessonId") UUID lessonId) {
        return this.queryGateway.query(new FindAllQuizzesByLessonIdQuery(lessonId),
                ResponseTypes.multipleInstancesOf(QuizDto.class)).join();
    }

    @GetMapping("/{id}/questions")
    public List<QuestionDto> getAllQuestions(@PathVariable("id") UUID id) {
        return this.queryGateway.query(new FindAllQuestionsByQuizIdQuery(id),
                ResponseTypes.multipleInstancesOf(QuestionDto.class)).join();
    }
}
