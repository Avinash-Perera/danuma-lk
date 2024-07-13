package com.avinash.danumalk.posts;

import com.avinash.danumalk.file.FileStorageService;
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
public class ImagePostHelper {

    private final FileStorageService fileStorageService;

    @Value("${application.file.uploads.base-path}")
    private String fileUploadBasePath;

    @Value("${application.file.uploads.temp-file-path}")
    private String tempUploadBasePath;

    @Value("${application.file.uploads.posts}")
    private String postUploadBasePath;

    @Value("${application.env.urls.app-url}")
    private String fileAccessBaseUrl;

    public String movePostImageToPostFolder(String imageName, UUID postId) {
        String tempFilePath = Paths.get(fileUploadBasePath, tempUploadBasePath, imageName).toString();
        File tempFile = new File(tempFilePath);

        if (!tempFile.exists()) {
            log.warn("Temp file does not exist: {}", tempFilePath);
            return null;
        }

        String postsFolderPath = Paths.get(fileUploadBasePath, postUploadBasePath , postId.toString()).toString();
        File postFolder = new File(postsFolderPath);

        if (!postFolder.exists()) {
            boolean folderCreated = postFolder.mkdirs();
            if (!folderCreated) {
                log.warn("Failed to create the user folder: {}", postsFolderPath);
                return null;
            }
        }

        String targetFilePath = Paths.get(postsFolderPath, imageName).toString();
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

    public boolean isImageInPostsDirectory(String imageName, UUID postId) {
        String postFilePath = Paths.get(fileUploadBasePath, postUploadBasePath, postId.toString(), imageName).toString();
        File userFile = new File(postFilePath);
        return userFile.exists();
    }

    public void deleteImageFromPostDirectory(String imageName, UUID postId) {
        String postFilePath = Paths.get(fileUploadBasePath, postUploadBasePath + postId + "/" , imageName).toString();
        File postFile = new File(postFilePath);
        if (postFile.exists()) {
            if (postFile.delete()) {
                log.info("Deleted image from post directory: {}", postFilePath);
            } else {
                log.warn("Failed to delete image from post directory: {}", postFilePath);
            }
        } else {
            log.warn("Image does not exist in post directory: {}", postFilePath);
        }
    }

    public String getImageUrl(String imageName, UUID postId) {
        return fileAccessBaseUrl + "/uploads/posts/"  + postId + "/" + imageName;
    }
}
