package com.avinash.danumalk.posts;

import com.avinash.danumalk.file.FileUploadResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
public class ImagePostResponse {
    private UUID id;
    private String title;
    private String description;
    private List<FileUploadResponse> imageUrls;
}