package com.example.bementora.mapper;

import com.example.bementora.dto.request.ModuleCreationRequest;
import com.example.bementora.dto.response.ModuleResponse;
import com.example.bementora.entity.ModuleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {CurriculumMapper.class})
public interface ModuleMapper {
    @Mapping(target = "moduleId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "curricula", ignore = true)
    @Mapping(target = "course", ignore = true)
    ModuleEntity toEntity(ModuleCreationRequest request);

    @Mapping(target = "curricula", qualifiedByName = "mapCurriculaList")
    ModuleResponse toResponse(ModuleEntity entity);

    List<ModuleResponse> toResponseList(List<ModuleEntity> entities);

    @Mapping(target = "moduleId", ignore = true)
    @Mapping(target = "courseId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "curricula", ignore = true)
    @Mapping(target = "course", ignore = true)
    void updateEntityFromRequest(ModuleCreationRequest request, @MappingTarget ModuleEntity entity);
}
