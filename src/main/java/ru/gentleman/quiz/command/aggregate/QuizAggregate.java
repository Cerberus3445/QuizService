package ru.gentleman.quiz.command.aggregate;

import lombok.extern.slf4j.Slf4j;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import ru.gentleman.common.dto.QuestionSnapshot;
import ru.gentleman.common.event.*;
import ru.gentleman.common.exception.NotFoundException;
import ru.gentleman.common.exception.ValidationException;
import ru.gentleman.quiz.command.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Aggregate
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class QuizAggregate {

    @AggregateIdentifier
    private UUID id;

    private UUID lessonId;

    private String title;

    private String description;

    private Map<UUID, QuestionSnapshot> questions;

    private Boolean isActive;

    public QuizAggregate() {

    }

    @CommandHandler
    public QuizAggregate(CreateQuizCommand command) {
        QuizCreatedEvent event = QuizCreatedEvent.builder()
                .id(command.id())
                .lessonId(command.lessonId())
                .title(command.title())
                .description(command.description())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuizCreatedEvent event) {
        this.id = event.id();
        this.lessonId = event.lessonId();
        this.title = event.title();
        this.description = event.description();
        this.questions = new HashMap<>();
        this.isActive = true;
    }

    @CommandHandler
    public void handle(CreateQuestionCommand command) {
        isQuizActive();

        QuestionCreatedEvent event = QuestionCreatedEvent.builder()
                .id(command.id())
                .quizId(command.quizId())
                .title(command.title())
                .description(command.description())
                .correctAnswer(command.correctAnswer())
                .explanation(command.explanation())
                .score(command.score())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuestionCreatedEvent event) {
        QuestionSnapshot createdQuestion = QuestionSnapshot.builder()
                .id(event.id())
                .title(event.title())
                .description(event.description())
                .correctAnswer(event.correctAnswer())
                .explanation(event.explanation())
                .quizId(event.quizId())
                .score(event.score())
                .isActive(true)
                .build();

        this.questions.put(event.id(), createdQuestion);
    }

    @CommandHandler
    public void handle(UpdateQuestionCommand command) {
        isQuizActive();

        QuestionSnapshot question = questions.get(command.id());
        if (question == null || !question.isActive()) {
            throw new NotFoundException("Вопрос не найден или удален", command.id());
        }

        QuestionUpdatedEvent event = QuestionUpdatedEvent.builder()
                .id(command.id())
                .quizId(command.quizId())
                .title(command.title())
                .description(command.description())
                .correctAnswer(command.correctAnswer())
                .explanation(command.explanation())
                .score(command.score())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuestionUpdatedEvent event) {
        QuestionSnapshot updatedQuestion = QuestionSnapshot.builder()
                .id(event.id())
                .title(event.title())
                .description(event.description())
                .correctAnswer(event.correctAnswer())
                .explanation(event.explanation())
                .quizId(event.quizId())
                .score(event.score())
                .isActive(true)
                .build();

        this.questions.replace(event.id(),updatedQuestion);
    }

    @CommandHandler
    public void handle(DeleteQuestionCommand command) {
        isQuizActive();

        if(questions.get(command.id()) == null) {
            throw new NotFoundException("вопрос не найден", command.id());
        }

        QuestionDeletedEvent event = new QuestionDeletedEvent(command.id());

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuestionDeletedEvent event) {
        QuestionSnapshot foundQuestion = this.questions.get(event.id());

        this.questions.replace(event.id(), QuestionSnapshot.builder()
                .id(event.id())
                .title(foundQuestion.title())
                .description(foundQuestion.description())
                .correctAnswer(foundQuestion.correctAnswer())
                .explanation(foundQuestion.explanation())
                .quizId(foundQuestion.quizId())
                .score(foundQuestion.score())
                .isActive(false)
                .build());
    }

    @CommandHandler
    public void handle(UpdateQuizCommand command) {
        isQuizActive();

        QuizUpdatedEvent event = QuizUpdatedEvent.builder()
                .id(command.id())
                .lessonId(command.lessonId())
                .title(command.title())
                .description(command.description())
                .build();

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuizUpdatedEvent event) {
        this.lessonId = event.lessonId();
        this.title = event.title();
        this.description = event.description();
    }

    @CommandHandler
    public void handle(DeleteQuizCommand command) {
        isQuizActive();

        QuizDeletedEvent event = new QuizDeletedEvent(command.id());

        AggregateLifecycle.apply(event);
    }

    @EventSourcingHandler
    public void on(QuizDeletedEvent quizDeletedEvent) {
        this.isActive = false;
    }

    private void isQuizActive() {
        if(!this.isActive) {
            throw new ValidationException("Тест был удалён, изменения невозможны");
        }
    }
}
