package com.lincoln.kilimaniThrive.models.entity;

import com.lincoln.kilimaniThrive.enums.PrayerRequestStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "prayer_requests")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class PrayerRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Optional

    @Column(nullable = false)
    private String phoneNumberOrRegCode;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String request;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PrayerRequestStatus status = PrayerRequestStatus.PENDING;

    @Column(nullable = false)
    @Builder.Default
    private int likes = 0;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
