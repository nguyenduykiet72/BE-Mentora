package com.example.bementora.service;

import com.example.bementora.dto.request.ModuleCreationRequest;
import com.example.bementora.dto.response.ModuleResponse;

import java.util.List;
import java.util.UUID;

public interface ModuleService {
    ModuleResponse createModule(ModuleCreationRequest request, UUID instructorId);

    ModuleResponse getModuleById(UUID moduleId, UUID instructorId);

    List<ModuleResponse> getModulesByCourse(UUID courseId, UUID instructorId);

    ModuleResponse updateModule(UUID moduleId, ModuleCreationRequest request, UUID instructorId);

    void deleteModule(UUID moduleId, UUID instructorId);

    List<ModuleResponse> reorderModules(UUID courseId, List<UUID> moduleIds, UUID instructorId);
}
