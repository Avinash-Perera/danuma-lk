package com.avinash.danumalk.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfilePicService {

    private final ProfilePicRepository profilePicRepository;
    private final UserRepository userRepository;

    public String uploadImage(User user, MultipartFile file) throws IOException {
        ProfilePic profilePic = profilePicRepository.save(ProfilePic.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ProfilePicUtils.compressImage(file.getBytes()))
                .user(user)
                .build());

        return "file uploaded successfully" + file.getOriginalFilename();
    }

}
