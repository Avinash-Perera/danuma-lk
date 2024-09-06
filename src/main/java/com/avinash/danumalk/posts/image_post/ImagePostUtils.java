package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.user.User;
import com.avinash.danumalk.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImagePostUtils {
    private final UserRepository userRepository;

    public User initializeOwner(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found!"));
    }
}
