package com.avinash.danumalk.profileImage;

import com.avinash.danumalk.user.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profileImage")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class ProfileImageController {
    private final ProfileImageService service;

    public ProfileImageController(ProfileImageService service) {
        this.service = service;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'user:create')")
    public ResponseEntity<?> uploadImage(@RequestParam("profileImage") MultipartFile file,
                                         @AuthenticationPrincipal User user) throws IOException {
        String uploadImage = service.uploadImage(file, user);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    public ResponseEntity<?> downloadImage(@AuthenticationPrincipal User user) {
        byte[] imageData = service.downloadImage(user);
        if (imageData != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(imageData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Profile image not found for the user.");
        }
    }

}
