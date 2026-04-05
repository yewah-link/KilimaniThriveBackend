package com.lincoln.kilimaniThrive.services;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.enums.PrayerRequestStatus;
import com.lincoln.kilimaniThrive.mappers.PrayerRequestMapper;
import com.lincoln.kilimaniThrive.models.dtos.PrayerRequestDTO;
import com.lincoln.kilimaniThrive.models.entity.PrayerRequest;
import com.lincoln.kilimaniThrive.repositories.PrayerRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrayerRequestServiceImpl implements PrayerRequestService {

    private final PrayerRequestRepository prayerRequestRepository;
    private final PrayerRequestMapper prayerRequestMapper;

    @Override
    @Transactional
    public GenericResponseV2<PrayerRequestDTO> submitRequest(PrayerRequestDTO requestDTO) {
        try {
            PrayerRequest prayerRequest = prayerRequestMapper.toEntity(requestDTO);
            prayerRequest.setStatus(PrayerRequestStatus.APPROVED); // Default to APPROVED for immediate visibility
            
            PrayerRequest saved = prayerRequestRepository.save(prayerRequest);
            log.info("New prayer request submitted: {}", saved.getId());
            
            return GenericResponseV2.<PrayerRequestDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Prayer request submitted successfully")
                    ._embedded(prayerRequestMapper.toDto(saved))
                    .build();
        } catch (Exception e) {
            log.error("Failed to submit prayer request: {}", e.getMessage());
            return GenericResponseV2.<PrayerRequestDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Could not submit prayer request")
                    .build();
        }
    }

    @Override
    public GenericResponseV2<List<PrayerRequestDTO>> getCommunityWallRequests() {
        try {
            // Show PENDING, APPROVED, and PRAYED_FOR requests on the wall in a single query
            List<PrayerRequestStatus> statuses = List.of(
                    PrayerRequestStatus.PENDING, 
                    PrayerRequestStatus.APPROVED, 
                    PrayerRequestStatus.PRAYED_FOR
            );
            List<PrayerRequest> wallRequests = prayerRequestRepository.findAllByStatusInOrderByCreatedAtDesc(statuses);
            
            return GenericResponseV2.<List<PrayerRequestDTO>>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Community wall fetched successfully")
                    ._embedded(prayerRequestMapper.toDtoList(wallRequests))
                    .build();
        } catch (Exception e) {
            log.error("Failed to fetch community wall: {}", e.getMessage());
            return GenericResponseV2.<List<PrayerRequestDTO>>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Could not fetch community wall")
                    .build();
        }
    }

    @Override
    public GenericResponseV2<List<PrayerRequestDTO>> getAllRequests() {
        try {
            List<PrayerRequest> allRequests = prayerRequestRepository.findAll();
            return GenericResponseV2.<List<PrayerRequestDTO>>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("All requests fetched successfully")
                    ._embedded(prayerRequestMapper.toDtoList(allRequests))
                    .build();
        } catch (Exception e) {
            return GenericResponseV2.<List<PrayerRequestDTO>>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Could not fetch all requests")
                    .build();
        }
    }

    @Override
    @Transactional
    public GenericResponseV2<PrayerRequestDTO> updateStatus(Long id, String status) {
        try {
            PrayerRequest request = prayerRequestRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Prayer request not found"));
            
            request.setStatus(PrayerRequestStatus.valueOf(status.toUpperCase()));
            PrayerRequest updated = prayerRequestRepository.save(request);
            
            return GenericResponseV2.<PrayerRequestDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Status updated to " + status)
                    ._embedded(prayerRequestMapper.toDto(updated))
                    .build();
        } catch (Exception e) {
            return GenericResponseV2.<PrayerRequestDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Could not update status: " + e.getMessage())
                    .build();
        }
    }

    @Override
    @Transactional
    public GenericResponseV2<PrayerRequestDTO> likeRequest(Long id) {
        try {
            PrayerRequest request = prayerRequestRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Prayer request not found"));
            
            request.setLikes(request.getLikes() + 1);
            PrayerRequest updated = prayerRequestRepository.save(request);
            
            log.info("Prayer request {} liked. Total likes: {}", id, updated.getLikes());
            
            return GenericResponseV2.<PrayerRequestDTO>builder()
                    .status(ResponseStatusEnum.SUCCESS)
                    .message("Increased prayer count")
                    ._embedded(prayerRequestMapper.toDto(updated))
                    .build();
        } catch (Exception e) {
            log.error("Failed to like prayer request {}: {}", id, e.getMessage());
            return GenericResponseV2.<PrayerRequestDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Could not update prayer count")
                    .build();
        }
    }
}
