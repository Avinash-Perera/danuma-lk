package com.avinash.danumalk.util;

import com.avinash.danumalk.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {
    // Helper method to get the authenticated user's ID
    public Integer getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {

            // Assuming your User class has a getId() method
            if (userDetails instanceof User) {
                return ((User) userDetails).getId();
            }
        }
        return null;
    }
}
