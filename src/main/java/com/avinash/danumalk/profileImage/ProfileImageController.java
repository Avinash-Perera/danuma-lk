package com.avinash.danumalk.profileImage;

import com.avinash.danumalk.user.User;
import com.avinash.danumalk.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/profileImage")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
@RequiredArgsConstructor
public class ProfileImageController {
    private final ProfileImageService profileImageService;
    private final UserRepository userRepository;



    @PostMapping
    public ResponseEntity<?> uploadImage(@RequestParam("profileImage") MultipartFile file, Authentication authentication) throws IOException {
        if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();

            // Ensure that the profile image is associated with the authenticated user
            String uploadImage = profileImageService.uploadImage(file, user);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(uploadImage);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User not authenticated.");
    }

    @GetMapping
    public ResponseEntity<?> getProfileImage(Authentication authentication) {
        if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();

            // Retrieve the profile image for the authenticated user
            byte[] imageData = profileImageService.getProfileImage(user);

            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))
                    .body(imageData);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User not authenticated.");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteProfileImage(Authentication authentication) {
        if (authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();

            // Delete the profile image for the authenticated user
            String result = profileImageService.deleteProfileImage(user);

            return ResponseEntity.status(HttpStatus.OK)
                    .body(result);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("User not authenticated.");
    }


}
