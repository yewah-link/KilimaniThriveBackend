package com.lincoln.kilimaniThrive.models.dtos;

import com.lincoln.kilimaniThrive.enums.PrayerRequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrayerRequestDTO {
    private Long id;
    private String name;
    private String phoneNumberOrRegCode;
    private String request;
    private PrayerRequestStatus status;
    private int likes;
    private LocalDateTime createdAt;
}
