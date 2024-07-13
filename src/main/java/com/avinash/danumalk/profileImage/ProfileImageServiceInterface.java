package com.avinash.danumalk.profileImage;

import com.avinash.danumalk.user.User;
import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageServiceInterface {
    String uploadImage(MultipartFile file, User user);

    byte[] getProfileImage(User user);

    String deleteProfileImage(User user);
}
