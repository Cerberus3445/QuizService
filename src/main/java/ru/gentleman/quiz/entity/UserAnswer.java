package ru.gentleman.quiz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "quiz", name = "user_answers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserAnswer {

    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    private UUID userId;

    private String answer;

    private Boolean isCorrect;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "quiz_attempt_id", referencedColumnName = "id")
    private QuizAttempt quizAttempt;
}
