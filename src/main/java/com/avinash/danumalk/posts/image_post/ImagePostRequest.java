package com.avinash.danumalk.posts.image_post;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;


public record ImagePostRequest(
        @NotNull @NotEmpty String description,
        @NotNull List<String> imageUrls,
        @NotNull  UUID postTypeId


) {}