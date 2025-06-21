package com.example.bementora.service.impl;

import com.example.bementora.dto.request.LectureCreationRequest;
import com.example.bementora.dto.response.LectureResponse;
import com.example.bementora.entity.CurriculumEntity;
import com.example.bementora.entity.LectureEntity;
import com.example.bementora.enums.CurriculumEnum;
import com.example.bementora.exception.InvalidCurriculumTypeException;
import com.example.bementora.exception.ResourceNotFoundException;
import com.example.bementora.mapper.LectureMapper;
import com.example.bementora.repository.CurriculumRepository;
import com.example.bementora.repository.LectureRepository;
import com.example.bementora.service.LectureService;
import com.example.bementora.service.S3Service;
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
public class LectureServiceImpl implements LectureService {
    private final LectureRepository lectureRepository;
    private final CurriculumRepository curriculumRepository;
    private final LectureMapper lectureMapper;
    private final S3Service s3Service;

    @Override
    public LectureResponse createLecture(LectureCreationRequest request, UUID instructorId) {
        log.info("Creating lecture for curriculum: {} by instructor: {}", request.getCurriculumId(), instructorId);

        validateCurriculumForLecture(request.getCurriculumId(), instructorId);

        LectureEntity lectureEntity = lectureMapper.toEntity(request);
        lectureEntity.setLectureId(UUID.randomUUID());

        LocalDateTime now = LocalDateTime.now();
        lectureEntity.setCreatedAt(now);
        lectureEntity.setUpdatedAt(now);

        LectureEntity savedLecture = lectureRepository.save(lectureEntity);

        log.info("Successfully created lecture: {} for curriculum: {}", savedLecture.getLectureId(), request.getCurriculumId());

        LectureResponse response = lectureMapper.toResponse(savedLecture);
        if (savedLecture.getVideoUrl() != null && !savedLecture.getVideoUrl().isEmpty()) {
            try {
                String presignedUrl = s3Service.generatePresignedDownloadUrl(savedLecture.getVideoUrl());
                response.setVideoUrl(presignedUrl);
            } catch (Exception e) {
                log.warn("Failed to generate presigned URL for lecture video: {}", e.getMessage());
                response.setVideoUrl(null);
            }
        }

        return response;
    }

    @Override
    @Transactional
    public LectureResponse getLectureById(UUID lectureId, UUID instructorId) {
        log.info("Fetching lecture: {} by instructor: {}", lectureId, instructorId);

        LectureEntity lecture = lectureRepository.findByIdAndInstructorId(lectureId,instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lecture not found with id: " + lectureId + " for instructor: " + instructorId));

        LectureResponse response = lectureMapper.toResponse(lecture);

        if (lecture.getVideoUrl() != null && !lecture.getVideoUrl().isEmpty()) {
            try {
                String presignedUrl = s3Service.generatePresignedDownloadUrl(lecture.getVideoUrl());
                response.setVideoUrl(presignedUrl);
            } catch (Exception e) {
                log.warn("Failed to generate presigned URL for lecture video: {}", e.getMessage());
                response.setVideoUrl(null);
            }
        }
        return response;
    }

    @Override
    public List<LectureResponse> getLecturesByCurriculum(UUID curriculumId, UUID instructorId) {
        log.info("Fetching lectures for curriculum: {} by instructor: {}", curriculumId, instructorId);

        validateCurriculumForLecture(curriculumId,instructorId);

        List<LectureEntity> lectures = lectureRepository.findByCurriculumIdAndInstructorId(curriculumId, instructorId);

        List<LectureResponse> responses = lectureMapper.toResponseList(lectures);

        responses.forEach(response -> {
            if (response.getVideoS3Key() != null && !response.getVideoS3Key().isEmpty()) {
                try {
                    String presignedUrl = s3Service.generatePresignedDownloadUrl(response.getVideoS3Key());
                    response.setVideoUrl(presignedUrl);
                } catch (Exception e) {
                    log.warn("Failed to generate presigned URL for lecture video: {}", e.getMessage());
                    response.setVideoUrl(null);
                }

            }
        });

        return responses;
    }

    @Override
    public LectureResponse updateLecture(UUID lectureId, LectureCreationRequest request, UUID instructorId) {
        log.info("Updating lecture: {} by instructor: {}", lectureId, instructorId);

        LectureEntity existingLecture = lectureRepository.findByIdAndInstructorId(lectureId, instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lecture not found with id: " + lectureId + " for instructor: " + instructorId));

        if (!existingLecture.getCurriculumId().equals(request.getCurriculumId())) {
            validateCurriculumForLecture(request.getCurriculumId(), instructorId);
        }

        lectureMapper.updateEntityFromRequest(request, existingLecture);
        existingLecture.setUpdatedAt(LocalDateTime.now());

        LectureEntity updatedLecture = lectureRepository.save(existingLecture);

        log.info("Successfully updated lecture: {}", lectureId);

        LectureResponse response = lectureMapper.toResponse(updatedLecture);
        if (updatedLecture.getVideoUrl() != null && !updatedLecture.getVideoUrl().isEmpty()) {
            try {
                String presignedUrl = s3Service.generatePresignedDownloadUrl(updatedLecture.getVideoUrl());
                response.setVideoUrl(presignedUrl);
            } catch (Exception e) {
                log.warn("Failed to generate presigned URL for lecture video: {}", e.getMessage());
                response.setVideoUrl(null);
            }
        }

        return response;
    }

    @Override
    public void deleteLecture(UUID lectureId, UUID instructorId) {
        log.info("Deleting lecture: {} by instructor: {}", lectureId, instructorId);

        LectureEntity lectureEntity = lectureRepository.findByIdAndInstructorId(lectureId, instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lecture not found with id: " + lectureId + " for instructor: " + instructorId));

        if (lectureEntity.getVideoUrl() != null && !lectureEntity.getVideoUrl().isEmpty()) {
            try {
                s3Service.deleteVideo(lectureEntity.getVideoUrl());
                log.info("Deleted video from S3: {}", lectureEntity.getVideoUrl());
            } catch (Exception e) {
                log.warn("Failed to delete video from S3: {}", e.getMessage());
                // Continue with lecture deletion even if S3 deletion fails
            }
        }

        lectureRepository.delete(lectureEntity);

        log.info("Successfully deleted lecture: {}", lectureId);
    }

    @Override
    public LectureResponse attachVideoToLecture(UUID lectureId, String s3Key, UUID instructorId) {
        log.info("Attaching video {} to lecture: {} by instructor: {}", s3Key, lectureId, instructorId);

        LectureEntity lecture = lectureRepository.findByIdAndInstructorId(lectureId, instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lecture not found with id: " + lectureId + " for instructor: " + instructorId));

        try {
            s3Service.confirmVideoUpload(s3Key,instructorId,lecture.getCurriculum().getModule().getCourseId());
        } catch (Exception e) {
            throw new ResourceNotFoundException("Video not found in S3: " + s3Key);
        }

        String oldVideoUrl = lecture.getVideoUrl();
        lecture.setVideoUrl(s3Key);
        lecture.setUpdatedAt(LocalDateTime.now());

        LectureEntity updatedLecture = lectureRepository.save(lecture);

        if (oldVideoUrl != null && !oldVideoUrl.isEmpty() && !oldVideoUrl.equals(s3Key)) {
            try {
                s3Service.deleteVideo(oldVideoUrl);
                log.info("Deleted video from S3: {}", oldVideoUrl);
            } catch (Exception e) {
                log.warn("Failed to delete old video from S3: {}", e.getMessage());
            }
        }

        log.info("Successfully attached video to lecture: {}", lectureId);

        LectureResponse response = lectureMapper.toResponse(updatedLecture);

        try {
            String presignedUrl = s3Service.generatePresignedDownloadUrl(s3Key);
            response.setVideoUrl(presignedUrl);
        } catch (Exception e) {
            log.warn("Failed to generate presigned URL for attached video: {}", e.getMessage());
            response.setVideoUrl(null);
        }

        return response;

    }

    @Override
    public String generateVideoViewUrl(UUID lectureId, UUID instructorId) {
        log.info("Generating video view URL for lecture: {} by instructor: {}", lectureId, instructorId);

        LectureEntity lectureEntity = lectureRepository.findByIdAndInstructorId(lectureId, instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lecture not found with id: " + lectureId + " for instructor: " + instructorId));

        if (lectureEntity.getVideoUrl() == null || lectureEntity.getVideoUrl().isEmpty()) {
            throw new ResourceNotFoundException("No video attached to lecture: " + lectureId);
        }

        try {
            String presignedUrl = s3Service.generatePresignedDownloadUrl(lectureEntity.getVideoUrl());
            log.info("Generated video view URL for lecture: {}", lectureId);
            return presignedUrl;
        } catch (Exception e) {
            log.error("Failed to generate video view URL for lecture: {}", lectureId, e);
            throw new RuntimeException("Failed to generate video view URL", e);
        }
    }

    private CurriculumEntity validateCurriculumForLecture(UUID curriculumId, UUID instructorId) {
        CurriculumEntity curriculum = curriculumRepository.findByIdAndInstructorId(curriculumId, instructorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Curriculum not found with id: " + curriculumId + " for instructor: " + instructorId));

        if (curriculum.getType() != CurriculumEnum.LECTURE) {
            throw new InvalidCurriculumTypeException(
                    "Curriculum " + curriculumId + " is not of type LECTURE. Cannot create lecture.");
        }

        return curriculum;
    }
}
