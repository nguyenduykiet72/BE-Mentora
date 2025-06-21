package com.example.bementora.service;

import com.example.bementora.dto.request.LectureCreationRequest;
import com.example.bementora.dto.response.LectureResponse;

import java.util.List;
import java.util.UUID;

public interface LectureService {
    LectureResponse createLecture(LectureCreationRequest request, UUID instructorId);

    LectureResponse getLectureById(UUID lectureId, UUID instructorId);

    List<LectureResponse> getLecturesByCurriculum(UUID curriculumId, UUID instructorId);

    LectureResponse updateLecture(UUID lectureId, LectureCreationRequest request, UUID instructorId);

    void deleteLecture(UUID lectureId, UUID instructorId);

    // Video management
    LectureResponse attachVideoToLecture(UUID lectureId, String s3Key, UUID instructorId);

    String generateVideoViewUrl(UUID lectureId, UUID instructorId);
}
