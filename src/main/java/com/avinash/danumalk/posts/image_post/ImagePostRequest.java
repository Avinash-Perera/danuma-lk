package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.posts.annotations.AtLeastOneImageUrl;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public record ImagePostRequest(
        @NotNull @NotEmpty String description,
        @NotNull @AtLeastOneImageUrl List<String> imageUrls,
        @NotNull UUID postTypeId,
        @NotNull @NotEmpty @Size(min = 1) List<UUID> postCategoryIds
) {

}
