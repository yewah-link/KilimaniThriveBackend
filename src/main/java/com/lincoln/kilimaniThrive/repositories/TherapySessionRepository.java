package com.lincoln.kilimaniThrive.repositories;

import com.lincoln.kilimaniThrive.enums.TherapySessionStatus;
import com.lincoln.kilimaniThrive.models.entity.TherapySession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TherapySessionRepository extends JpaRepository<TherapySession, Long> {
    List<TherapySession> findByStatus(TherapySessionStatus status);
}
