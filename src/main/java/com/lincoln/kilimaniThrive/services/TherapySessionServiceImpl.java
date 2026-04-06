package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.enums.TherapySessionStatus;
import com.lincoln.kilimaniThrive.mappers.TherapySessionMapper;
import com.lincoln.kilimaniThrive.models.dtos.TherapySessionRequestDto;
import com.lincoln.kilimaniThrive.models.dtos.TherapySessionResponseDto;
import com.lincoln.kilimaniThrive.models.entity.TherapySession;
import com.lincoln.kilimaniThrive.repositories.TherapySessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TherapySessionServiceImpl implements TherapySessionService {

    private final TherapySessionRepository repository;
    private final TherapySessionMapper mapper;

    @Override
    public TherapySessionResponseDto createSession(TherapySessionRequestDto requestDto) {
        TherapySession entity = mapper.toEntity(requestDto);
        entity.setStatus(TherapySessionStatus.PENDING);
        TherapySession savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }

    @Override
    public List<TherapySessionResponseDto> getAllSessions() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TherapySessionResponseDto> getSessionsByStatus(TherapySessionStatus status) {
        return repository.findByStatus(status).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public TherapySessionResponseDto updateSessionStatus(Long id, TherapySessionStatus newStatus) {
        TherapySession session = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Therapy session not found with ID: " + id));
        session.setStatus(newStatus);
        TherapySession updatedSession = repository.save(session);
        return mapper.toDto(updatedSession);
    }
}
