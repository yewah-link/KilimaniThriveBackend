package com.lincoln.kilimaniThrive.repositories;

import com.lincoln.kilimaniThrive.enums.PrayerRequestStatus;
import com.lincoln.kilimaniThrive.models.entity.PrayerRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrayerRequestRepository extends JpaRepository<PrayerRequest, Long> {
    List<PrayerRequest> findAllByStatusOrderByCreatedAtDesc(PrayerRequestStatus status);
    List<PrayerRequest> findAllByStatusInOrderByCreatedAtDesc(java.util.Collection<PrayerRequestStatus> statuses);
}
