package com.avinash.danumalk.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class ProfilePictureController {

    private final ProfilePictureService profilePictureService;

    @PostMapping("/{userId}/profile-picture")
    public ResponseEntity<?> uploadProfilePicture(
            @PathVariable Long userId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        profilePictureService.uploadProfilePicture(userId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Long userId) {
        byte[] image = profilePictureService.getProfilePicture(userId);
        return ResponseEntity.ok().body(image);
    }

    @DeleteMapping("/{userId}/profile-picture")
    public ResponseEntity<?> deleteProfilePicture(@PathVariable Long userId) {
        profilePictureService.deleteProfilePicture(userId);
        return ResponseEntity.ok().build();
    }
}