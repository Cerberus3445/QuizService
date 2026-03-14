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
import ru.gentleman.quiz.command.CreateQuizCommand;
import ru.gentleman.quiz.command.DeleteQuizCommand;
import ru.gentleman.quiz.command.UpdateQuizCommand;
import ru.gentleman.quiz.dto.QuizDto;

import java.net.URI;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/quizzes")
@RequiredArgsConstructor
public class QuizCommandController {

    private final CommandGateway commandGateway;

    private final MessageSource messageSource;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid QuizDto quizDto,
                                         BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        UUID id = UUID.randomUUID();
        CreateQuizCommand command = CreateQuizCommand.builder()
                .id(id)
                .lessonId(quizDto.lessonId())
                .title(quizDto.title())
                .description(quizDto.description())
                .build();

        this.commandGateway.sendAndWait(command);

        return ResponseEntity
                .created(URI.create("/api/v1/quizzes/" + id))
                .body(
                        this.messageSource.getMessage(
                                "info.quiz.created",
                                new Object[]{id},
                                Locale.getDefault()
                        )
                );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") UUID id,
                                         @RequestBody @Valid QuizDto quizDto,
                                         BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        UpdateQuizCommand command = UpdateQuizCommand.builder()
                .id(id)
                .lessonId(quizDto.lessonId())
                .title(quizDto.title())
                .description(quizDto.description())
                .build();

        this.commandGateway.sendAndWait(command);

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.quiz.updated",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        DeleteQuizCommand command = new DeleteQuizCommand(id);

        this.commandGateway.sendAndWait(command);

        return ResponseEntity.noContent().build();
    }
}
