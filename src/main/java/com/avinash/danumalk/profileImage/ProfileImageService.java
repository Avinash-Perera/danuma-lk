package com.avinash.danumalk.profileImage;


import com.avinash.danumalk.user.User;
import com.avinash.danumalk.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ProfileImageService {


    private final ProfileImageRepository repository;
    private final UserRepository userRepository;

    public String uploadImage(MultipartFile file, User user) throws IOException {
        // Check if the user already has a profile image
        if (user.getProfileImage() != null) {
            // Handle the case where the user already has a profile image
            return "User already has a profile image.";
        }

        ProfileImage profileImage = repository.save(ProfileImage.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ProfileImageUtils.compressImage(file.getBytes()))
                .user(user) // Set the associated user
                .build());

        // Set the profile image for the user
        user.setProfileImage(profileImage);

        return "File uploaded successfully: " + file.getOriginalFilename();
    }



    @Transactional
    public byte[] downloadImage(User user) {
        Optional<ProfileImage> dbImageData = repository.findByUser(user);
        return dbImageData.map(profileImage -> ProfileImageUtils.decompressImage(profileImage.getImageData()))
                .orElse(null);
    }
}