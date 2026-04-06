package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.enums.TherapySessionStatus;
import com.lincoln.kilimaniThrive.models.dtos.TherapySessionRequestDto;
import com.lincoln.kilimaniThrive.models.dtos.TherapySessionResponseDto;

import java.util.List;

public interface TherapySessionService {
    TherapySessionResponseDto createSession(TherapySessionRequestDto requestDto);
    List<TherapySessionResponseDto> getAllSessions();
    List<TherapySessionResponseDto> getSessionsByStatus(TherapySessionStatus status);
    TherapySessionResponseDto updateSessionStatus(Long id, TherapySessionStatus newStatus);
}
