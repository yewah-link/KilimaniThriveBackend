package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.PrayerRequestDTO;
import com.lincoln.kilimaniThrive.models.entity.PrayerRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrayerRequestMapper {

    PrayerRequestDTO toDto(PrayerRequest entity);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    PrayerRequest toEntity(PrayerRequestDTO dto);

    List<PrayerRequestDTO> toDtoList(List<PrayerRequest> entities);
}
