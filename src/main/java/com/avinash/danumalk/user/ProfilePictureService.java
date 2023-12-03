package com.avinash.danumalk.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProfilePictureService {
    private final UserRepository userRepository;
    private final ProfilePictureRepository profilePictureRepository;

    public void uploadProfilePicture(Long userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));

        ProfilePicture profilePicture = ProfilePicture.builder()
                .imageData(file.getBytes())
                .build();

        user.setProfilePicture(profilePicture);
        userRepository.save(user);
    }

    public byte[] getProfilePicture(Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));

        ProfilePicture profilePicture = user.getProfilePicture();
        return (profilePicture != null) ? profilePicture.getImageData() : null;
    }

    public void deleteProfilePicture(Long userId) {
        User user = userRepository.findById(Math.toIntExact(userId)).orElseThrow(() -> new RuntimeException("User not found"));

        user.setProfilePicture(null);
        userRepository.save(user);
    }
}
