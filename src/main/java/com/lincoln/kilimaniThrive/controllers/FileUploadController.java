package com.lincoln.kilimaniThrive.controllers;

import com.lincoln.kilimaniThrive.common.GenericResponseV2;
import com.lincoln.kilimaniThrive.common.ResponseStatusEnum;
import com.lincoln.kilimaniThrive.models.dtos.MediaResponseDTO;
import com.lincoln.kilimaniThrive.services.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    public ResponseEntity<GenericResponseV2<MediaResponseDTO>> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            log.info("Received file upload request: {}", file.getOriginalFilename());
            String fileUrl = fileStorageService.saveFile(file);

            MediaResponseDTO response = MediaResponseDTO.builder()
                    .url(fileUrl)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .fileSize(file.getSize())
                    .build();

            return ResponseEntity.ok(
                    GenericResponseV2.<MediaResponseDTO>builder()
                            .status(ResponseStatusEnum.SUCCESS)
                            .message("File uploaded successfully")
                            ._embedded(response)
                            .build()
            );
        } catch (Exception e) {
            log.error("File upload failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(
                    GenericResponseV2.<MediaResponseDTO>builder()
                            .status(ResponseStatusEnum.ERROR)
                            .message("Could not upload file: " + e.getMessage())
                            ._embedded(null)
                            .build()
            );
        }
    }
}
