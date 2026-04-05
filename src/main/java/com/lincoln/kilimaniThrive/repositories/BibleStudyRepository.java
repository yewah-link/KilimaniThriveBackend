package com.lincoln.kilimaniThrive.repositories;

import com.lincoln.kilimaniThrive.enums.StudyStatus;
import com.lincoln.kilimaniThrive.models.entity.BibleStudy;
import com.lincoln.kilimaniThrive.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BibleStudyRepository extends JpaRepository<BibleStudy, Long> {
    Page<BibleStudy> findByStatus(StudyStatus status, Pageable pageable);
    Page<BibleStudy> findByCategory(String category, Pageable pageable);
    Optional<BibleStudy> findBySlug(String slug);
    Page<BibleStudy> findByAuthor(User author, Pageable pageable);
}
