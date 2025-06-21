package com.example.bementora.mapper;

import com.example.bementora.dto.request.CurriculumCreationRequest;
import com.example.bementora.dto.response.CurriculumResponse;
import com.example.bementora.entity.CurriculumEntity;
import com.example.bementora.enums.CurriculumEnum;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", uses = {LectureMapper.class, QuizMapper.class})
public interface CurriculumMapper {

    @Mapping(target = "curriculumId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "module", ignore = true)
    @Mapping(target = "curriculumProgress", ignore = true)
    @Mapping(target = "discussions", ignore = true)
    @Mapping(target = "lectures", ignore = true)
    @Mapping(target = "quizzes", ignore = true)
    CurriculumEntity toEntity(CurriculumCreationRequest request);

    @Mapping(target = "lectures", expression = "java(mapLecturesConditionally(entity))")
    @Mapping(target = "quizzes", expression = "java(mapQuizzesConditionally(entity))")
    CurriculumResponse toResponse(CurriculumEntity entity);

    @Named("mapCurriculaList")
    List<CurriculumResponse> toResponseList(List<CurriculumEntity> entities);

    @Mapping(target = "curriculumId", ignore = true)
    @Mapping(target = "moduleId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "module", ignore = true)
    @Mapping(target = "curriculumProgress", ignore = true)
    @Mapping(target = "discussions", ignore = true)
    @Mapping(target = "lectures", ignore = true)
    @Mapping(target = "quizzes", ignore = true)
    void updateEntityFromRequest(CurriculumCreationRequest request, @MappingTarget CurriculumEntity entity);

    // Custom mapping methods
    default List<com.example.bementora.dto.response.LectureResponse> mapLecturesConditionally(CurriculumEntity entity) {
        if (entity.getType() == CurriculumEnum.LECTURE && entity.getLectures() != null) {
            LectureMapper lectureMapper = new LectureMapperImpl();
            return lectureMapper.toResponseList(entity.getLectures());
        }
        return null;
    }

    default List<com.example.bementora.dto.response.QuizResponse> mapQuizzesConditionally(CurriculumEntity entity) {
        if (entity.getType() == CurriculumEnum.QUIZ && entity.getQuizzes() != null) {
            QuizMapper quizMapper = new QuizMapperImpl();
            return quizMapper.toResponseList(entity.getQuizzes());
        }
        return null;
    }
}