package com.avinash.danumalk.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank(message = "User name cannot be blank")
        @Size(max = 50, message = "User name must be less than 50 characters")
        String usersName,

        @Email(message = "Email should be valid")


        String profile_image_url
) {
}
