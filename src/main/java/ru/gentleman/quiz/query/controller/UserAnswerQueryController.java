package ru.gentleman.quiz.query.controller;

import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.*;
import ru.gentleman.common.exception.NotFoundException;
import ru.gentleman.quiz.dto.UserAnswerDto;
import ru.gentleman.quiz.query.FindAllUserAnswersByUserIdQuery;
import ru.gentleman.quiz.query.FindUserAnswerByIdQuery;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-answers")
@RequiredArgsConstructor
public class UserAnswerQueryController {

    private final QueryGateway queryGateway;

    @GetMapping("/{id}")
    public UserAnswerDto get(@PathVariable("id") UUID id) {
        UserAnswerDto userAnswerDto = this.queryGateway.query(new FindUserAnswerByIdQuery(id),
                ResponseTypes.instanceOf(UserAnswerDto.class)).join();

        if(userAnswerDto == null) {
            throw new NotFoundException("error.user_answer.not_found_id", id);
        }

        return userAnswerDto;
    }

    @GetMapping(params = "userId")
    public List<UserAnswerDto> getAllByUserId(@RequestParam("userId") UUID userId) {
        return  this.queryGateway.query(
                new FindAllUserAnswersByUserIdQuery(userId),
                ResponseTypes.multipleInstancesOf(UserAnswerDto.class)).
                join();
    }
}
