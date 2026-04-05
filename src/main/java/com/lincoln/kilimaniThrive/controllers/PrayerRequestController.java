package com.lincoln.kilimaniThrive.controllers;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.models.dtos.PrayerRequestDTO;
import com.lincoln.kilimaniThrive.services.PrayerRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prayer-requests")
@RequiredArgsConstructor
@Slf4j
public class PrayerRequestController {

    private final PrayerRequestService prayerRequestService;

    /**
     * Submit a new prayer request. Open to any caller (visitor or registered).
     */
    @PostMapping
    public ResponseEntity<GenericResponseV2<PrayerRequestDTO>> createPrayerRequest(@RequestBody PrayerRequestDTO requestDTO) {
        log.info("Received public prayer request submission");
        GenericResponseV2<PrayerRequestDTO> response = prayerRequestService.submitRequest(requestDTO);
        
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get approved requests for the community wall. 
     * Restricted to registered users (Authenticated).
     */
    @GetMapping("/community")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericResponseV2<List<PrayerRequestDTO>>> getCommunityPrayers() {
        log.info("Fetching community prayer requests for authenticated user");
        GenericResponseV2<List<PrayerRequestDTO>> response = prayerRequestService.getCommunityWallRequests();
        return ResponseEntity.ok(response);
    }

    /**
     * Admin endpoint to see all requests and manage their status.
     */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseV2<List<PrayerRequestDTO>>> getAllRequests() {
        return ResponseEntity.ok(prayerRequestService.getAllRequests());
    }

    @PutMapping("/admin/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponseV2<PrayerRequestDTO>> updateRequestStatus(
            @PathVariable Long id,
            @RequestParam String status) {
        try {
            return ResponseEntity.ok(prayerRequestService.updateStatus(id, status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(GenericResponseV2.<PrayerRequestDTO>builder()
                    .status(ResponseStatusEnum.ERROR)
                    .message("Could not update status: " + e.getMessage())
                    .build());
        }
    }


    /**
     * Public endpoint to like (thumbs-up) a prayer request. 
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<GenericResponseV2<PrayerRequestDTO>> likeRequest(@PathVariable Long id) {
        log.info("Received prayer request like for ID: {}", id);
        GenericResponseV2<PrayerRequestDTO> response = prayerRequestService.likeRequest(id);
        if (response.getStatus() == ResponseStatusEnum.SUCCESS) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }
}
