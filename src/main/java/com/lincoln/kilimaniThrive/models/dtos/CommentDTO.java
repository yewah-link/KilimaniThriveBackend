package com.lincoln.kilimaniThrive.models.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    private String content;
    private String authorName;
    private String authorEmail;
    private String authorInitial;
    private Long blogId;
    private Long parentId;
    private LocalDateTime createdAt;
}
