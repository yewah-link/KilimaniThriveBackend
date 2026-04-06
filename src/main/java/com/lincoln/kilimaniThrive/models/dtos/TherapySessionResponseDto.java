package com.lincoln.kilimaniThrive.models.dtos;

import com.lincoln.kilimaniThrive.enums.TherapySessionStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TherapySessionResponseDto {
    private Long id;
    private String name;
    private String phoneNumberOrEmail;
    private String topic;
    private String notes;
    private LocalDateTime preferredDate;
    private TherapySessionStatus status;
    private LocalDateTime createdAt;
}
