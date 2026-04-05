package com.lincoln.kilimaniThrive.models.dtos;

import com.lincoln.kilimaniThrive.enums.StudyStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BibleStudyDTO {
    private Long id;
    private String title;
    private String description;
    private String content;
    private String contentUrl;
    private String imageUrl;
    private String slug;
    private String category;
    private StudyStatus status;
    private Long views;
    private Long likes;
    private Long authorId;
    private String authorName;
    private List<TagDTO> tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
