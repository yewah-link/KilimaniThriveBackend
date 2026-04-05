package com.lincoln.kilimaniThrive.models.entity;

import com.lincoln.kilimaniThrive.enums.StudyStatus;
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
@Table(name = "bible_studies")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class BibleStudy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String content; // Optional if not using URL

    private String contentUrl; // PDF, video, etc.

    private String imageUrl; // thumbnail

    private String slug;

    private String category;

    @Enumerated(EnumType.STRING)
    private StudyStatus status = StudyStatus.DRAFT;

    // Engagement
    private Long views = 0L;
    private Long likes = 0L;

    // Author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    // Tags (optional but recommended)
    @ManyToMany
    @JoinTable(
            name = "bible_study_tags",
            joinColumns = @JoinColumn(name = "bible_study_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}