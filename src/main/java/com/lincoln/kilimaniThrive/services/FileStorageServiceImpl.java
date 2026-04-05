package com.lincoln.kilimaniThrive.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    public FileStorageServiceImpl(@Value("${file.upload-dir:uploads/}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("Upload directory initialized: {}", fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public String saveFile(MultipartFile file) throws IOException {
        // Normalize file name
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = "";

        try {
            if (originalFileName.contains("..")) {
                throw new RuntimeException("Sorry! Filename contains invalid path sequence " + originalFileName);
            }

            // Extract extension
            int lastDotIndex = originalFileName.lastIndexOf(".");
            if (lastDotIndex != -1) {
                fileExtension = originalFileName.substring(lastDotIndex);
            }

            // Generate unique file name
            String fileName = UUID.randomUUID().toString() + fileExtension;

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            log.info("File saved: {} -> {}", originalFileName, fileName);

            // Return the relative URL (mapped via StaticResourceConfig)
            return "/uploads/" + fileName;

        } catch (IOException ex) {
            log.error("Could not store file {}. Please try again!", originalFileName, ex);
            throw ex;
        }
    }

    @Override
    public boolean deleteFile(String fileUrl) {
        try {
            if (fileUrl == null || !fileUrl.startsWith("/uploads/")) {
                return false;
            }

            String fileName = fileUrl.replace("/uploads/", "");
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();

            return Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            log.error("Error deleting file: {}", fileUrl, ex);
            return false;
        }
    }
}
