package ru.gentleman.quiz.command.interceptor;

import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.messaging.MessageDispatchInterceptor;
import org.springframework.stereotype.Component;
import ru.gentleman.common.util.ExceptionUtils;
import ru.gentleman.quiz.command.CreateUserAnswerCommand;
import ru.gentleman.quiz.command.DeleteQuizAttemptCommand;
import ru.gentleman.quiz.command.FinishQuizAttemptCommand;
import ru.gentleman.quiz.repository.QuizAttemptRepository;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

@Component
@RequiredArgsConstructor
public class QuizAttemptInterceptor implements MessageDispatchInterceptor<CommandMessage<?>> {

    private final QuizAttemptRepository quizAttemptRepository;

    @Nonnull
    @Override
    public BiFunction<Integer, CommandMessage<?>, CommandMessage<?>> handle(@Nonnull List<? extends CommandMessage<?>> messages) {
        return (index, command) -> {
            if(DeleteQuizAttemptCommand.class.equals(command.getPayloadType())) {
                DeleteQuizAttemptCommand deleteQuizAttemptCommand = (DeleteQuizAttemptCommand) command.getPayload();

                if(!this.quizAttemptRepository.existsById(deleteQuizAttemptCommand.id())){
                    ExceptionUtils.alreadyExists("error.quiz_attempt.not_found", deleteQuizAttemptCommand.id());
                }
            } else if (CreateUserAnswerCommand.class.equals(command.getPayloadType())) {
                CreateUserAnswerCommand createUserAnswerCommand = (CreateUserAnswerCommand) command.getPayload();

                if(!this.quizAttemptRepository.existsById(createUserAnswerCommand.quizAttemptId())){
                    ExceptionUtils.alreadyExists("error.quiz_attempt.not_found", createUserAnswerCommand.quizAttemptId());
                }
            }  else if (FinishQuizAttemptCommand.class.equals(command.getPayloadType())) {
                FinishQuizAttemptCommand finishQuizAttemptCommand = (FinishQuizAttemptCommand) command.getPayload();

                if(!this.quizAttemptRepository.existsById(finishQuizAttemptCommand.id())){
                    ExceptionUtils.alreadyExists("error.details.not_found", finishQuizAttemptCommand.id());
                }
            }

            return command;
        };
    }
}
