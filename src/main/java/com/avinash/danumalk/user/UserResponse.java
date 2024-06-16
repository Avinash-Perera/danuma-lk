package com.avinash.danumalk.user;

import com.avinash.danumalk.file.FileUploadResponse;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private Integer id;
    private String usersName;
    private String email;
    private FileUploadResponse profile_image_url;
    private boolean enabled;

}
