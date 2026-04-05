package com.lincoln.kilimaniThrive.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MediaResponseDTO {
    private String url;
    private String fileName;
    private String fileType;
    private Long fileSize;
}
