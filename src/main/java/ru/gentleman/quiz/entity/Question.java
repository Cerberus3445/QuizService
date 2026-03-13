package ru.gentleman.quiz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "quiz", name = "questions")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    private String title;

    private String description;

    private String correctAnswer;

    private String explanation;

    private int score;

    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "quiz_id", referencedColumnName = "id")
    private Quiz quiz;
}
