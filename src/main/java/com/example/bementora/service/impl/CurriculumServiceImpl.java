package com.example.bementora.service.impl;

import com.example.bementora.dto.request.CurriculumCreationRequest;
import com.example.bementora.dto.response.CurriculumResponse;
import com.example.bementora.entity.CurriculumEntity;
import com.example.bementora.enums.CurriculumEnum;
import com.example.bementora.exception.DuplicateOrderIndexException;
import com.example.bementora.exception.ResourceNotFoundException;
import com.example.bementora.mapper.CurriculumMapper;
import com.example.bementora.repository.CurriculumRepository;
import com.example.bementora.repository.ModuleRepository;
import com.example.bementora.service.CurriculumService;
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
public class CurriculumServiceImpl implements CurriculumService {
    private final CurriculumRepository curriculumRepository;
    private final ModuleRepository moduleRepository;
    private final CurriculumMapper curriculumMapper;

    @Override
    public CurriculumResponse createCurriculum(CurriculumCreationRequest request, UUID instructorId) {
        log.info("Creating curriculum for module: {} by instructor: {}", request.getModuleId(), instructorId);

        validateModuleOwnership(request.getModuleId(), instructorId);

        validateOrderIndexUniqueness(request.getModuleId(), request.getOrderIndex(), instructorId, null);

        CurriculumEntity curriculumEntity = curriculumMapper.toEntity(request);
        curriculumEntity.setCurriculumId(UUID.randomUUID());

        LocalDateTime now = LocalDateTime.now();
        curriculumEntity.setCreatedAt(now);
        curriculumEntity.setUpdatedAt(now);

        CurriculumEntity savedCurriculum = curriculumRepository.save(curriculumEntity);

        log.info("Successfully created curriculum: {} for module: {}", savedCurriculum.getCurriculumId(), request.getModuleId());

        return curriculumMapper.toResponse(savedCurriculum);
    }

    @Override
    @Transactional
    public CurriculumResponse getCurriculumById(UUID curriculumId, UUID instructorId) {
        log.info("Fetching curriculum: {} by instructor: {}", curriculumId, instructorId);

        CurriculumEntity curriculumEntity = curriculumRepository.findByIdAndInstructorId(curriculumId, instructorId )
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Curriculum not found with id: " + curriculumId + " for instructor: " + instructorId));

        return curriculumMapper.toResponse(curriculumEntity);
    }

    @Override
    @Transactional
    public List<CurriculumResponse> getCurriculumsByModule(UUID moduleId, UUID instructorId) {
        log.info("Fetching curricula for module: {} by instructor: {}", moduleId, instructorId);

        validateModuleOwnership(moduleId, instructorId);

        List<CurriculumEntity> curriculum = curriculumRepository.findByModuleIdAndInstructorId(moduleId, instructorId);

        return curriculumMapper.toResponseList(curriculum);
    }

    @Override
    public CurriculumResponse updateCurriculum(UUID curriculumId, CurriculumCreationRequest request, UUID instructorId) {
        log.info("Updating curriculum: {} by instructor: {}", curriculumId, instructorId);

        CurriculumEntity existingCurriculum = curriculumRepository.findByIdAndInstructorId(curriculumId, instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Curriculum not found with id: " + curriculumId + " for instructor: " + instructorId
                ));

        if (!existingCurriculum.getModuleId().equals(request.getModuleId())) {
            validateModuleOwnership(request.getModuleId(), instructorId);
        }

        if (!existingCurriculum.getOrderIndex().equals(request.getOrderIndex()) ||
            !existingCurriculum.getModuleId().equals(request.getModuleId())) {
            validateOrderIndexUniqueness(request.getModuleId(), request.getOrderIndex(), instructorId, curriculumId);
        }

        if (!existingCurriculum.getType().equals(request.getType())) {
            validateTypeChange(existingCurriculum, request.getType());
        }

        curriculumMapper.updateEntityFromRequest(request, existingCurriculum);
        existingCurriculum.setUpdatedAt(LocalDateTime.now());

        CurriculumEntity updatedCurriculum = curriculumRepository.save(existingCurriculum);

        log.info("Successfully updated curriculum: {}", curriculumId);

        return curriculumMapper.toResponse(updatedCurriculum);

    }

    @Override
    public void deleteCurriculum(UUID curriculumId, UUID instructorId) {
        log.info("Deleting curriculum: {} by instructor: {}", curriculumId, instructorId);

        CurriculumEntity curriculumEntity = curriculumRepository.findByIdAndInstructorId(curriculumId, instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Curriculum not found with id: " + curriculumId + " for instructor: " + instructorId));

        curriculumRepository.delete(curriculumEntity);

        log.info("Successfully deleted curriculum: {}", curriculumId);
    }

    private void validateModuleOwnership(UUID moduleId, UUID instructorId) {
        boolean exists = moduleRepository.findByIdAndInstructorId(moduleId, instructorId).isPresent();

        if (!exists) {
            throw new ResourceNotFoundException(
                    "Module not found with id: " + moduleId + " for instructor: " + instructorId + " or the module is not owned by the instructor.");
        }
    }

    private void validateOrderIndexUniqueness(UUID moduleId, Integer orderIndex, UUID instructorId, UUID excludeCurriculumId) {
        boolean exists = curriculumRepository.existsByOrderIndexInModule(moduleId, orderIndex, instructorId, excludeCurriculumId);

        if (exists) {
            throw new DuplicateOrderIndexException(
                    "Order index " + orderIndex + " already exists in module: " + moduleId);
        }
    }

    private void validateTypeChange(CurriculumEntity existingCurriculum, CurriculumEnum newType) {
        switch (existingCurriculum.getType()) {
            case LECTURE:
                if (newType == CurriculumEnum.QUIZ &&
                existingCurriculum.getLectures() != null && !existingCurriculum.getLectures().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Cannot change curriculum type from LECTURE to QUIZ because it contains lectures. Delete lectures first.");
                }
                break;
            case QUIZ:
                if (newType == CurriculumEnum.LECTURE &&
                        existingCurriculum.getQuizzes() != null && !existingCurriculum.getQuizzes().isEmpty()) {
                    throw new IllegalArgumentException(
                            "Cannot change curriculum type from QUIZ to LECTURE because it contains quizzes. Delete quizzes first.");
                }
                break;
        }
    }
}
