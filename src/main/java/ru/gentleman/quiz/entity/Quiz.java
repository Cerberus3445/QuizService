package ru.gentleman.quiz.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(schema = "quiz", name = "quizzes")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Quiz {

    @Id
    @UuidGenerator
    @GeneratedValue
    private UUID id;

    private String title;

    private String description;

    private UUID lessonId;

    @OneToMany(mappedBy = "quiz")
    private List<Question> questions;

    private Boolean isActive;
}
