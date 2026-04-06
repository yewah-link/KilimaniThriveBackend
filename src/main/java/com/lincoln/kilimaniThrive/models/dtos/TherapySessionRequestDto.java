package com.lincoln.kilimaniThrive.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TherapySessionRequestDto {

    private String name;

    @NotBlank(message = "Contact information is required")
    private String phoneNumberOrEmail;

    @NotBlank(message = "Session topic is required")
    private String topic;

    @NotBlank(message = "Notes are required")
    @Size(min = 10, message = "Please provide more detail in your notes")
    private String notes;

    private LocalDateTime preferredDate;
}
