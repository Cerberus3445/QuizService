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
import ru.gentleman.quiz.command.CreateQuestionCommand;
import ru.gentleman.quiz.command.DeleteQuestionCommand;
import ru.gentleman.quiz.command.UpdateQuestionCommand;
import ru.gentleman.quiz.dto.QuestionDto;
import ru.gentleman.quiz.service.QuizService;

import java.net.URI;
import java.util.Locale;
import java.util.UUID;


@RestController
@RequestMapping("/api/v1/questions")
@RequiredArgsConstructor
public class QuestionCommandController {

    private final CommandGateway commandGateway;

    private final MessageSource messageSource;

    private final QuizService quizService;

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid QuestionDto questionDto,
                                         BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        UUID id = UUID.randomUUID();
        CreateQuestionCommand command = CreateQuestionCommand.builder()
                .id(id)
                .quizId(questionDto.quizId())
                .title(questionDto.title())
                .description(questionDto.description())
                .correctAnswer(questionDto.correctAnswer())
                .explanation(questionDto.explanation())
                .score(questionDto.score())
                .build();

        this.commandGateway.sendAndWait(command);

        return ResponseEntity
                .created(URI.create("/api/v1/questions/" + id))
                .body(
                        this.messageSource.getMessage(
                                "info.question.created",
                                new Object[]{id},
                                Locale.getDefault()
                        )
                );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable("id") UUID id,
                                         @RequestBody @Valid QuestionDto questionDto,
                                         BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()) throw new ValidationException(ValidationErrorUtils.collectErrorsToString(
                bindingResult.getFieldErrors()
        ));

        UpdateQuestionCommand command = UpdateQuestionCommand.builder()
                .id(id)
                .quizId(questionDto.quizId())
                .title(questionDto.title())
                .description(questionDto.description())
                .correctAnswer(questionDto.correctAnswer())
                .explanation(questionDto.explanation())
                .score(questionDto.score())
                .build();

        this.commandGateway.sendAndWait(command);

        return ResponseEntity.ok(
                this.messageSource.getMessage(
                        "info.question.updated",
                        null,
                        Locale.getDefault()
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {
        UUID quizId = this.quizService.getQuizIdByQuestionId(id);

        DeleteQuestionCommand command = new DeleteQuestionCommand(id, quizId);

        this.commandGateway.sendAndWait(command);

        return ResponseEntity.noContent().build();
    }
}
