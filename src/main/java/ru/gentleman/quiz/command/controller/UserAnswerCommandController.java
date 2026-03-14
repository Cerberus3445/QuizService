package ru.gentleman.quiz.command.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.gentleman.common.exception.ValidationException;
import ru.gentleman.common.util.ValidationErrorUtils;
import ru.gentleman.quiz.command.CreateUserAnswerCommand;
import ru.gentleman.quiz.dto.UserAnswerDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/user-answers")
@RequiredArgsConstructor
public class UserAnswerCommandController {

    private final CommandGateway commandGateway;

    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid UserAnswerDto userAnswerDto,
                                         BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        UUID id = UUID.randomUUID();
        CreateUserAnswerCommand command = CreateUserAnswerCommand.builder()
                .id(id)
                .userId(userAnswerDto.userId())
                .questionId(userAnswerDto.questionId())
                .quizAttemptId(userAnswerDto.quizAttemptId())
                .answer(userAnswerDto.answer())
                .createdAt(LocalDateTime.now())
                .build();

        this.commandGateway.sendAndWait(command);

        return ResponseEntity
                .created(URI.create("/api/v1/user-answers/" + id))
                .body(
                        this.messageSource.getMessage(
                                "info.user_answer.created",
                                new Object[]{id},
                                Locale.getDefault()
                        )
                );
    }
}
