package com.lincoln.kilimaniThrive.services;

import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

public interface FileStorageService {
    /**
     * Saves a multipart file to the configured upload directory.
     * @param file The file to save.
     * @return The URL/path that can be used to access the file via the StaticResourceConfig.
     * @throws IOException If something goes wrong with saving the file.
     */
    String saveFile(MultipartFile file) throws IOException;

    /**
     * Deletes a file from the upload directory.
     * @param fileUrl The full URL or relative path to the file.
     * @return true if successful.
     */
    boolean deleteFile(String fileUrl);
}
