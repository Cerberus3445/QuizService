package ru.gentleman.quiz.command.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.gentleman.common.exception.ValidationException;
import ru.gentleman.common.util.ValidationErrorUtils;
import ru.gentleman.quiz.command.CreateQuizAttemptCommand;
import ru.gentleman.quiz.command.DeleteQuizAttemptCommand;
import ru.gentleman.quiz.command.FinishQuizAttemptCommand;
import ru.gentleman.quiz.dto.QuizAttemptDto;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quiz-attempts")
@RequiredArgsConstructor
public class QuizAttemptCommandController {

    private final CommandGateway commandGateway;

    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid QuizAttemptDto quizAttemptDto,
                                         BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        UUID id = UUID.randomUUID();
        CreateQuizAttemptCommand command = CreateQuizAttemptCommand.builder()
                .id(id)
                .quizId(quizAttemptDto.quizId())
                .userId(quizAttemptDto.userId())
                .createdAt(LocalDateTime.now())
                .build();

        this.commandGateway.sendAndWait(command);

        return ResponseEntity
                .created(URI.create("/api/v1/quiz-attempts/" + id))
                .body(
                        this.messageSource.getMessage(
                                "info.quiz_attempt.created",
                                new Object[]{id},
                                Locale.getDefault()
                        )
                );
    }

    @PatchMapping("/{id}/finish")
    public ResponseEntity<String> finish(@PathVariable("id") UUID id) {
        FinishQuizAttemptCommand command = new FinishQuizAttemptCommand(id);

        this.commandGateway.sendAndWait(command);

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.quiz_attempt.finished",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        DeleteQuizAttemptCommand command = new DeleteQuizAttemptCommand(id);

        this.commandGateway.sendAndWait(command);

        return ResponseEntity.noContent().build();
    }

}
