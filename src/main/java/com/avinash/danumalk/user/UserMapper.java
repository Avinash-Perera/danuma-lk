package com.avinash.danumalk.user;


import com.avinash.danumalk.file.FileUploadResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserMapper {
    private final ImageHelper imageHelper;

    public User updateUserFromRequest(UserRequest request, User user) {
        user.setUsersName(request.usersName());
        user.setProfile_image_url(request.profile_image_url());
        var v =  imageHelper.isImageInTempDirectory(request.profile_image_url());
        if(v){
            System.out.println("Its in ");
        }

        return user;
    }

    public UserResponse toUserResponse(User user) {
        FileUploadResponse profileImageResponse = null;
        if (user.getProfile_image_url() != null) {
            String imageUrl = imageHelper.getImageUrl(user.getProfile_image_url());
            profileImageResponse = new FileUploadResponse(user.getProfile_image_url(), imageUrl);
        }

        return UserResponse.builder()
                .id(user.getId())
                .usersName(user.getUsersName())
                .email(user.getEmail())
                .profile_image_url(profileImageResponse)
                .enabled(user.isEnabled())
                .build();
    }
}
