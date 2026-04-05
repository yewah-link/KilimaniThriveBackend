package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.models.dtos.PrayerRequestDTO;
import java.util.List;

public interface PrayerRequestService {
    GenericResponseV2<PrayerRequestDTO> submitRequest(PrayerRequestDTO requestDTO);
    GenericResponseV2<List<PrayerRequestDTO>> getCommunityWallRequests();
    GenericResponseV2<List<PrayerRequestDTO>> getAllRequests(); // For Admin
    GenericResponseV2<PrayerRequestDTO> updateStatus(Long id, String status);
    GenericResponseV2<PrayerRequestDTO> likeRequest(Long id);
}
