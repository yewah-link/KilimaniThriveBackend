package com.lincoln.kilimaniThrive.models.entity;

import com.lincoln.kilimaniThrive.enums.BlogStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "blogs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Blogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Core content
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    // Media
    private String featuredImage;

    // Status
    @Enumerated(EnumType.STRING)
    private BlogStatus status;

    private boolean isFeatured;
    private boolean allowComments;

    // Engagement
    @Builder.Default
    private Long views = 0L;

    @Builder.Default
    private Long likes = 0L;

    // Timestamps
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime publishedAt;

    // Relationships
     @ManyToOne(fetch = FetchType.LAZY)
     private User author;

    @OneToMany(mappedBy = "blog")
    private List<Comment> comments;

    @ManyToMany
    private List<Tag> tags;
}