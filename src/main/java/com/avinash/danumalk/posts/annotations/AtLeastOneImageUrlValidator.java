package com.avinash.danumalk.posts.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;

public class AtLeastOneImageUrlValidator implements ConstraintValidator<AtLeastOneImageUrl, List<String>> {
    @Override
    public boolean isValid(List<String> imageUrls, ConstraintValidatorContext context) {
        // Check if the list is null or empty
        if (imageUrls == null || imageUrls.isEmpty()) {
            return false; // Invalid if the list is null or empty
        }

        // Check that none of the image URLs are empty strings
        for (String url : imageUrls) {
            if (url == null || url.trim().isEmpty()) {
                return false; // Invalid if any URL is null or empty
            }
        }

        return true; // Valid if all checks pass
    }
}
