package com.avinash.danumalk.posts;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;


public record ImagePostRequest(
        @NotNull @NotEmpty String title,
        @NotNull @NotEmpty String description,
        @NotNull List<String> imageUrls

) {}