package com.lincoln.kilimaniThrive.controllers;

import com.lincoln.kilimaniThrive.enums.TherapySessionStatus;
import com.lincoln.kilimaniThrive.models.dtos.TherapySessionRequestDto;
import com.lincoln.kilimaniThrive.models.dtos.TherapySessionResponseDto;
import com.lincoln.kilimaniThrive.services.TherapySessionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sessions")
@RequiredArgsConstructor
public class TherapySessionController {

    private final TherapySessionService sessionService;

    @PostMapping
    public ResponseEntity<TherapySessionResponseDto> bookSession(@Valid @RequestBody TherapySessionRequestDto requestDto) {
        TherapySessionResponseDto responseDto = sessionService.createSession(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TherapySessionResponseDto>> getAllSessions() {
        return ResponseEntity.ok(sessionService.getAllSessions());
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TherapySessionResponseDto>> getSessionsByStatus(@PathVariable TherapySessionStatus status) {
        return ResponseEntity.ok(sessionService.getSessionsByStatus(status));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TherapySessionResponseDto> updateSessionStatus(
            @PathVariable Long id,
            @RequestParam TherapySessionStatus status) {
        return ResponseEntity.ok(sessionService.updateSessionStatus(id, status));
    }
}
