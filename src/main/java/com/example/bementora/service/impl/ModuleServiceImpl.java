package com.example.bementora.service.impl;

import com.example.bementora.dto.request.ModuleCreationRequest;
import com.example.bementora.dto.response.ModuleResponse;
import com.example.bementora.entity.CoursesEntity;
import com.example.bementora.entity.ModuleEntity;
import com.example.bementora.exception.DuplicateOrderIndexException;
import com.example.bementora.exception.InstructorNotOwnerException;
import com.example.bementora.exception.ResourceNotFoundException;
import com.example.bementora.mapper.ModuleMapper;
import com.example.bementora.repository.CourseRepository;
import com.example.bementora.repository.ModuleRepository;
import com.example.bementora.service.ModuleService;
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
public class ModuleServiceImpl implements ModuleService {
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final ModuleMapper moduleMapper;

    @Override
    public ModuleResponse createModule(ModuleCreationRequest request, UUID instructorId) {
        log.info("Creating module for course: {} by instructor: {}", request.getCourseId(), instructorId);

        validateCourseOwnership(request.getCourseId(), instructorId);

        Integer finalOrderIndex  = request.getOrderIndex();
        if (finalOrderIndex == null || moduleRepository.existsByOrderIndexInCourse(
                request.getCourseId(), finalOrderIndex, instructorId, null
        )) {
            finalOrderIndex = getNextAvailableOrderIndex(request.getCourseId(), instructorId);
            log.info("Auto-assigned orderIndex: {} for course: {}", finalOrderIndex, request.getCourseId());

        }

        ModuleEntity moduleEntity = moduleMapper.toEntity(request);
        moduleEntity.setModuleId(UUID.randomUUID());
        moduleEntity.setOrderIndex(finalOrderIndex);

        LocalDateTime now = LocalDateTime.now();
        moduleEntity.setCreatedAt(now);
        moduleEntity.setUpdatedAt(now);

        ModuleEntity savedModule = moduleRepository.save(moduleEntity);
        log.info("Successfully created module: {} for course: {}", savedModule.getModuleId(), request.getCourseId());

        return moduleMapper.toResponse(savedModule);
    }

    @Override
    public ModuleResponse getModuleById(UUID moduleId, UUID instructorId) {
        log.info("Getting module with id: {} for instructor: {}", moduleId, instructorId);

        ModuleEntity moduleEntity = moduleRepository.findByIdAndInstructorId(moduleId, instructorId).orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + moduleId + " for instructor: " + instructorId));

        return moduleMapper.toResponse(moduleEntity);
    }

    @Override
    public List<ModuleResponse> getModulesByCourse(UUID courseId, UUID instructorId) {
        log.info("Getting modules for course: {} by instructor: {}", courseId, instructorId);

        validateCourseOwnership(courseId, instructorId);

        List<ModuleEntity> modules = moduleRepository.findByCourseIdAndInstructorId(courseId, instructorId);

        return moduleMapper.toResponseList(modules);
    }

    @Override
    public ModuleResponse updateModule(UUID moduleId, ModuleCreationRequest request, UUID instructorId) {
        log.info("Updating module: {} by instructor: {}", moduleId, instructorId);

        ModuleEntity existingModule = moduleRepository.findByIdAndInstructorId(moduleId, instructorId).orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + moduleId + " for instructor: " + instructorId));

        if (!existingModule.getCourseId().equals(request.getCourseId())) {
            validateCourseOwnership(request.getCourseId(), instructorId);
        }

        if (!existingModule.getOrderIndex().equals(request.getOrderIndex()) || !existingModule.getCourseId().equals(request.getCourseId())) {
            validateOrderIndexUniqueness(request.getCourseId(), request.getOrderIndex(), instructorId, moduleId);
        }

        moduleMapper.updateEntityFromRequest(request, existingModule);
        existingModule.setUpdatedAt(LocalDateTime.now());

        ModuleEntity updatedModule = moduleRepository.save(existingModule);

        log.info("Successfully updated module: {}", moduleId);

        return moduleMapper.toResponse(updatedModule);
    }

    @Override
    public void deleteModule(UUID moduleId, UUID instructorId) {
        log.info("Deleting module: {} by instructor: {}", moduleId, instructorId);

        ModuleEntity moduleEntity = moduleRepository.findByIdAndInstructorId(moduleId, instructorId).orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + moduleId + " for instructor: " + instructorId));

        moduleRepository.delete(moduleEntity);

        log.info("Successfully deleted module: {}", moduleId);

    }

    @Override
    public List<ModuleResponse> reorderModules(UUID courseId, List<UUID> moduleIds, UUID instructorId) {
        log.info("Reordering modules for course: {} by instructor: {}", courseId, instructorId);

        validateCourseOwnership(courseId, instructorId);

        List<ModuleEntity> modules = moduleRepository.findByCourseIdAndInstructorId(courseId, instructorId);

        if (modules.size() != moduleIds.size()) {
            throw new IllegalArgumentException("Module count mismatch. Expected: " + modules.size() + ", Actual: " + moduleIds.size());
        }

        List<UUID> existingModuleIds = modules.stream().map(ModuleEntity::getModuleId).toList();

        for (UUID moduleId : moduleIds) {
            if (!existingModuleIds.contains(moduleId)) {
                throw new ResourceNotFoundException("Module not found with id: " + moduleId);
            }
        }

        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < moduleIds.size(); i++) {
            UUID moduleId = moduleIds.get(i);
            ModuleEntity moduleEntity = modules.stream().filter(module -> module.getModuleId().equals(moduleId)).findFirst().orElseThrow(() -> new ResourceNotFoundException("Module not found with id: " + moduleId));

            moduleEntity.setOrderIndex(i + 1);
            moduleEntity.setUpdatedAt(now);
        }

        List<ModuleEntity> updatedModules = moduleRepository.saveAll(modules);

        log.info("Successfully reordered {} modules for course: {}", moduleIds.size(), courseId);

        return moduleMapper.toResponseList(updatedModules);
    }

    private CoursesEntity validateCourseOwnership(UUID courseId, UUID instructorId) {
        CoursesEntity course = courseRepository.findById(courseId).orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));

        if (!course.getInstructorId().equals(instructorId)) {
            throw new InstructorNotOwnerException("Instructor does not own course: " + courseId);
        }

        return course;
    }

    private void validateOrderIndexUniqueness(UUID courseId, Integer orderIndex, UUID instructorId, UUID excludeModuleId) {
        boolean exists = moduleRepository.existsByOrderIndexInCourse(courseId, orderIndex, instructorId, excludeModuleId);

        if (exists) {
            throw new DuplicateOrderIndexException("Order index " + orderIndex + " already exists in course: " + courseId);
        }
    }

    private Integer getNextAvailableOrderIndex(UUID courseId, UUID instructorId) {
        long moduleCount = moduleRepository.countByCourseIdAndInstructorId(courseId, instructorId);
        return (int) (moduleCount + 1);
    }
}
