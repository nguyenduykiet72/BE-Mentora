package com.example.bementora.mapper;

import com.example.bementora.dto.response.UserCreationResponse;
import com.example.bementora.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "userId", target = "id")
    UserCreationResponse entityToResponse(UserEntity userEntity);

    List<UserCreationResponse> entityToResponse(List<UserEntity> userEntities);
}
