package ru.gentleman.quiz.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gentleman.quiz.entity.Quiz;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, UUID> {

    List<Quiz> findAllByLessonId(UUID lessonId);
    @Query(value = """
                    SELECT quiz.quizzes.id FROM quiz.quizzes
                    INNER JOIN quiz.questions ON quiz.quizzes.id = quiz.questions.quiz_id
                    WHERE quiz.questions.id = :questionId
                    """,
            nativeQuery = true)
    Optional<UUID> findByQuestionId(@Param("questionId") UUID questionId);
}
