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


    @Transactional
    public String uploadImage(MultipartFile file, User user) {
        try {
            // Check if the user already has a profile image
            if (user.getProfileImage() != null) {
                return "User already has a profile image.";
            }

            ProfileImage profileImage = repository.save(ProfileImage.builder()
                    .name(file.getOriginalFilename())
                    .type(file.getContentType())
                    .imageData(ProfileImageUtils.compressImage(file.getBytes()))
                    .user(user)
                    .build());

            // Set the profile image for the user
            user.setProfileImage(profileImage);

            return "File uploaded successfully: " + file.getOriginalFilename();
        } catch (IOException e) {
            // Handle the exception (e.g., log it or return an error message)
            return "Error uploading the file: " + e.getMessage();
        }
    }

    public byte[] getProfileImage(User user) {
        try {
            // Retrieve the profile image for the authenticated user
            Optional<ProfileImage> dbImageData = repository.findByUser(user);

            // Check if the user has a profile image
            if (dbImageData.isPresent()) {
                return ProfileImageUtils.decompressImage(dbImageData.get().getImageData());
            } else {
                // Handle the case where the user does not have a profile image
                return new byte[0];
            }
        } catch (Exception e) {
            // Handle the exception (e.g., log it or return an error message)
            return new byte[0];
        }
    }



    @Transactional
    public String deleteProfileImage(User user) {
        try {
            // Retrieve the profile image for the authenticated user
            Optional<ProfileImage> dbImageData = repository.findByUser(user);

            // Check if the user has a profile image
            if (dbImageData.isPresent()) {
                // Remove the profile image reference from the user
                user.setProfileImage(null);
                userRepository.save(user);

                // Delete the profile image from the repository
                repository.delete(dbImageData.get());

                return "Profile image deleted successfully.";
            } else {
                // Handle the case where the user does not have a profile image
                return "User does not have a profile image to delete.";
            }
        } catch (Exception e) {
            // Handle the exception (e.g., log it or return an error message)
            return "Error deleting the profile image: " + e.getMessage();
        }
    }

}