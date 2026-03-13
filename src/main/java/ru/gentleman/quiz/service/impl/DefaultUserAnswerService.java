package ru.gentleman.quiz.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gentleman.quiz.dto.UserAnswerDto;
import ru.gentleman.quiz.mapper.UserAnswerMapper;
import ru.gentleman.quiz.repository.UserAnswerRepository;
import ru.gentleman.quiz.service.UserAnswerService;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class DefaultUserAnswerService implements UserAnswerService  {

    private final UserAnswerRepository userAnswerRepository;

    private final UserAnswerMapper userAnswerMapper;

    @Override
    public void create(UserAnswerDto dto) {
        log.info("create {}", dto);

        this.userAnswerRepository.save(
                this.userAnswerMapper.toEntity(dto)
        );
    }
}
