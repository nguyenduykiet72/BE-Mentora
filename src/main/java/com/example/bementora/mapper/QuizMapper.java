package com.example.bementora.mapper;

import com.example.bementora.dto.request.QuizCreationRequest;
import com.example.bementora.dto.response.QuizResponse;
import com.example.bementora.entity.QuizEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuizMapper {

    @Mapping(target = "quizId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "quizAttempts", ignore = true)
    @Mapping(target = "curriculum", ignore = true)
    QuizEntity toEntity(QuizCreationRequest request);

    @Mapping(target = "questionCount", expression = "java(entity.getQuestions() != null ? entity.getQuestions().size() : 0)")
    QuizResponse toResponse(QuizEntity entity);

    List<QuizResponse> toResponseList(List<QuizEntity> entities);

    @Mapping(target = "quizId", ignore = true)
    @Mapping(target = "curriculumId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "questions", ignore = true)
    @Mapping(target = "quizAttempts", ignore = true)
    @Mapping(target = "curriculum", ignore = true)
    void updateEntityFromRequest(QuizCreationRequest request, @MappingTarget QuizEntity entity);
}