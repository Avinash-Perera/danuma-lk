package com.avinash.danumalk.posts;

import com.avinash.danumalk.user.User;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ImagePostUtils {
    public User initializeOwner(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        User owner = new User();
        owner.setId(userId);
        return owner;
    }
}
