package com.example.bementora.mapper;

import com.example.bementora.dto.request.LectureCreationRequest;
import com.example.bementora.dto.response.LectureResponse;
import com.example.bementora.entity.LectureEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LectureMapper {

    @Mapping(target = "lectureId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "lectureProgress", ignore = true)
    @Mapping(target = "curriculum", ignore = true)
    @Mapping(target = "duration", source = "durationTime")
    LectureEntity toEntity(LectureCreationRequest request);

    @Mapping(target = "videoS3Key", source = "videoUrl")
    @Mapping(target = "videoUrl", ignore = true) // Will be set by service with presigned URL
    LectureResponse toResponse(LectureEntity entity);

    List<LectureResponse> toResponseList(List<LectureEntity> entities);

    @Mapping(target = "lectureId", ignore = true)
    @Mapping(target = "curriculumId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "lectureProgress", ignore = true)
    @Mapping(target = "curriculum", ignore = true)
    @Mapping(target = "duration", source = "durationTime")
    void updateEntityFromRequest(LectureCreationRequest request, @MappingTarget LectureEntity entity);
}