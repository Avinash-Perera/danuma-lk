package com.avinash.danumalk.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class ImageHelper {

    @Value("${application.file.uploads.base-path}")
    private String fileUploadBasePath;

    @Value("${application.file.uploads.temp-file-path}")
    private String tempUploadBasePath;

    @Value("${application.file.uploads.user-path}")
    private String userUploadBasePath;

    @Value("${application.env.urls.app-url}")
    private String fileAccessBaseUrl; // Base URL for accessing files

    public String moveProfileImageToUserFolder(String imageName, UUID userId) {
        String tempFilePath = Paths.get(fileUploadBasePath, tempUploadBasePath, imageName).toString();
        File tempFile = new File(tempFilePath);

        if (!tempFile.exists()) {
            log.warn("Temp file does not exist: {}", tempFilePath);
            return null;
        }

        String userFolderPath = Paths.get(fileUploadBasePath, userUploadBasePath , userId.toString()).toString();
        File userFolder = new File(userFolderPath);

        if (!userFolder.exists()) {
            boolean folderCreated = userFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Failed to create the user folder: {}", userFolderPath);
                return null;
            }
        }

        String targetFilePath = Paths.get(userFolderPath, imageName).toString();
        try {
            Files.move(tempFile.toPath(), Paths.get(targetFilePath));
            log.info("File moved to: {}", targetFilePath);
            return targetFilePath;
        } catch (IOException e) {
            log.error("Failed to move the file", e);
            return null;
        }
    }

    public boolean isImageInTempDirectory(String imageName) {
        String tempFilePath = Paths.get(fileUploadBasePath, tempUploadBasePath, imageName).toString();
        File tempFile = new File(tempFilePath);
        return tempFile.exists();
    }

    public boolean isImageInUserDirectory(String imageName, UUID userId) {
        String userFilePath = Paths.get(fileUploadBasePath, userUploadBasePath, userId.toString(), imageName).toString();
        File userFile = new File(userFilePath);
        return userFile.exists();
    }

    public void deleteImageFromUserDirectory(String imageName, UUID userId) {
        String userFilePath = Paths.get(fileUploadBasePath, userUploadBasePath, userId.toString(), imageName).toString();
        File userFile = new File(userFilePath);
        if (userFile.exists()) {
            if (userFile.delete()) {
                log.info("Deleted image from user directory: {}", userFilePath);
            } else {
                log.warn("Failed to delete image from user directory: {}", userFilePath);
            }
        } else {
            log.warn("Image does not exist in user directory: {}", userFilePath);
        }
    }

    public String getImagePath(String imageName, Integer userId) {
        return Paths.get(userUploadBasePath, userId.toString(), imageName).toString();
    }

    public String getImageUrl(String imageName, UUID userId) {
        return fileAccessBaseUrl + "/uploads/users/" + userId + "/" + imageName;
    }
}
