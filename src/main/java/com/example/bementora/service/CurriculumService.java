package com.example.bementora.service;

import com.example.bementora.dto.request.CurriculumCreationRequest;
import com.example.bementora.dto.response.CurriculumResponse;

import java.util.List;
import java.util.UUID;

public interface CurriculumService {
    CurriculumResponse createCurriculum(CurriculumCreationRequest request, UUID instructorId);

    CurriculumResponse getCurriculumById(UUID curriculumId, UUID instructorId);

    List<CurriculumResponse> getCurriculumsByModule(UUID moduleId, UUID instructorId);

    CurriculumResponse updateCurriculum(UUID curriculumId, CurriculumCreationRequest request, UUID instructorId);

    void deleteCurriculum(UUID curriculumId, UUID instructorId);

}
