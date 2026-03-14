package ru.gentleman.quiz.query.controller;

import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gentleman.common.exception.NotFoundException;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.query.FindQuestionByIdQuery;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionQueryController {

    private final QueryGateway queryGateway;

    @GetMapping("/{id}")
    public QuestionDto get(@PathVariable("id") UUID id) {
        QuestionDto questionDto = this.queryGateway.query(new FindQuestionByIdQuery(id),
                ResponseTypes.instanceOf(QuestionDto.class)).join();

        if(questionDto == null) {
            throw new NotFoundException("error.question.not_found_id", id);
        }

        return questionDto;
    }
}
