package com.lincoln.kilimaniThrive.models.dtos;

import com.lincoln.kilimaniThrive.enums.BlogStatus;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlogDTO {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName; // Flattened name
    private String featuredImage;
    private BlogStatus status;
    private Boolean isFeatured;
    private Boolean allowComments;
    private Long views;
    private Long likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime publishedAt;
    private List<TagDTO> tags;
}
