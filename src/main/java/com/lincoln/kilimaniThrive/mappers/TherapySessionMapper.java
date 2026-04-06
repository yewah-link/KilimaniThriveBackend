package com.lincoln.kilimaniThrive.mappers;

import com.lincoln.kilimaniThrive.models.dtos.TherapySessionRequestDto;
import com.lincoln.kilimaniThrive.models.dtos.TherapySessionResponseDto;
import com.lincoln.kilimaniThrive.models.entity.TherapySession;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TherapySessionMapper {

    TherapySession toEntity(TherapySessionRequestDto dto);

    TherapySessionResponseDto toDto(TherapySession entity);
}
