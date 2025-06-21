package com.example.bementora.service.impl;

import com.example.bementora.dto.request.QuizCreationRequest;
import com.example.bementora.dto.response.QuizResponse;
import com.example.bementora.entity.CurriculumEntity;
import com.example.bementora.entity.QuizEntity;
import com.example.bementora.enums.CurriculumEnum;
import com.example.bementora.exception.InvalidCurriculumTypeException;
import com.example.bementora.exception.ResourceNotFoundException;
import com.example.bementora.mapper.QuizMapper;
import com.example.bementora.repository.CurriculumRepository;
import com.example.bementora.repository.QuizRepository;
import com.example.bementora.service.QuizService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QuizServiceImpl implements QuizService {
    private final QuizRepository quizRepository;
    private final CurriculumRepository curriculumRepository;
    private final QuizMapper quizMapper;

    @Override
    public QuizResponse createQuiz(QuizCreationRequest request, UUID instructorId) {
        log.info("Creating quiz for curriculum: {} by instructor: {}", request.getCurriculumId(), instructorId);

        validateCurriculumnForQuiz(request.getCurriculumId(), instructorId);

        validateQuizBussinessRules(request);

        QuizEntity quizEntity = quizMapper.toEntity(request);
        quizEntity.setQuizId(UUID.randomUUID());

        LocalDateTime now = LocalDateTime.now();
        quizEntity.setCreatedAt(now);
        quizEntity.setUpdatedAt(now);

        QuizEntity savedQuiz = quizRepository.save(quizEntity);

        log.info("Successfully created quiz: {} for curriculum: {}", savedQuiz.getQuizId(), request.getCurriculumId());

        return quizMapper.toResponse(savedQuiz);
    }

    @Override
    @Transactional
    public QuizResponse getQuizById(UUID quizId, UUID instructorId) {
        log.info("Getting quiz with id: {} for instructor: {}", quizId, instructorId);

        QuizEntity quizEntity = quizRepository.findByIdAndInstructorId(quizId, instructorId)
                .orElseThrow(() -> new RuntimeException(
                        "Quiz not found with id: " + quizId + " for instructor: " + instructorId));

        return quizMapper.toResponse(quizEntity);
    }

    @Override
    public List<QuizResponse> getQuizzesByCurriculum(UUID curriculumId, UUID instructorId) {
        log.info("Getting quizzes for curriculum: {} by instructor: {}", curriculumId, instructorId);

        validateCurriculumnForQuiz(curriculumId, instructorId);

        List<QuizEntity> quizEntities = quizRepository.findByCurriculumIdAndInstructorId(curriculumId, instructorId);

        return quizMapper.toResponseList(quizEntities);
    }

    @Override
    public QuizResponse updateQuiz(UUID quizId, QuizCreationRequest request, UUID instructorId) {
        log.info("Updating quiz: {} by instructor: {}", quizId, instructorId);

        QuizEntity existingQuiz = quizRepository.findByIdAndInstructorId(quizId,instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                   "Quiz not found with id" + quizId + " for instructor: " + instructorId
                ));

        if (!existingQuiz.getCurriculumId().equals(request.getCurriculumId())) {
            validateCurriculumnForQuiz(request.getCurriculumId(), instructorId);
        }

        validateQuizBussinessRules(request);

        quizMapper.updateEntityFromRequest(request, existingQuiz);
        existingQuiz.setUpdatedAt(LocalDateTime.now());

        QuizEntity updatedQuiz = quizRepository.save(existingQuiz);

        log.info("Successfully updated quiz: {}", quizId);

        return quizMapper.toResponse(updatedQuiz);
    }

    @Override
    public void deleteQuiz(UUID quizId, UUID instructorId) {
        log.info("Deleting quiz: {} by instructor: {}", quizId, instructorId);

        QuizEntity quizEntity = quizRepository.findByIdAndInstructorId(quizId, instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Quiz not found with id: " + quizId + " for instructor: " + instructorId));

        if (quizEntity.getQuizAttempts() != null && !quizEntity.getQuizAttempts().isEmpty()) {
            throw new IllegalStateException(
                    "Cannot delete quiz with id: " + quizId + " because it has quiz attempts."
            );
        }

        quizRepository.delete(quizEntity);

        log.info("Deleted successfully quiz {}", quizId);
    }

    private void validateCurriculumnForQuiz(UUID curriculumId, UUID instructorId) {
        CurriculumEntity curriculum = curriculumRepository.findByIdAndInstructorId(curriculumId,instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Curriculum not found with id: " + curriculumId + " for instructor: " + instructorId + " or the curriculum is not owned by the instructor."
                ));

        if (curriculum.getType() != CurriculumEnum.QUIZ) {
            throw new InvalidCurriculumTypeException(
                    "Cannot create quiz for curriculum with id: " + curriculumId + " because it is not a quiz curriculum."
            );

        }
    }

    private void validateQuizBussinessRules(QuizCreationRequest request) {
        if (request.getPassingScore() != null && request.getPassingScore() < 0 || request.getPassingScore() > 100 ) {
            throw new IllegalArgumentException("Passing score must be between 0 and 100");
        }

        if (request.getTimeLimit() != null && request.getTimeLimit() < 0) {
            throw new IllegalArgumentException("Time limit cannot be negative");
        }

        if (request.getTitle().length() > 255) {
            throw new IllegalArgumentException("Title cannot be longer than 255 characters");
        }
    }



}
