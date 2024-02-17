package com.avinash.danumalk.util;

import com.avinash.danumalk.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.AccessDeniedException;

public class SecurityUtils {
    // Helper method to get the authenticated user's ID
    public static Integer getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            // Assuming your UserDetails implementation has a method getId()
            // If not, replace getId() with the appropriate method or property
            return ((User) authentication.getPrincipal()).getId();
        }
        return null;
    }

    // Helper method to validate if the authenticated user matches the user in the DTO
    public static void validateAuthenticatedUser(Integer userId) {
        Integer authenticatedUserId = getAuthenticatedUserId();
        if (authenticatedUserId == null || !authenticatedUserId.equals(userId)) {
            throw new AccessDeniedException("Access is denied");
        }
    }
}
