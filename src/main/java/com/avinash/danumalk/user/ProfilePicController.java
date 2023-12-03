package com.avinash.danumalk.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users/profile-pic")
@RequiredArgsConstructor
public class ProfilePicController {
    private final ProfilePicService service;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> uploadImage(
            @RequestParam("email") String email,
            @RequestParam("image") MultipartFile file
    ) throws IOException {
        // Find the user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

        // Save the profile picture associated with the user
        String uploadImage = service.uploadImage(user, file);
        return ResponseEntity.status(HttpStatus.OK)
                .body(uploadImage);
    }




}
