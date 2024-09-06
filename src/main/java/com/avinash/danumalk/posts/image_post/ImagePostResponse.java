package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.file.FileUploadResponse;
import com.avinash.danumalk.posts.BasePostEntityResponse;
import com.avinash.danumalk.posts.PostTypeResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@SuperBuilder
public class ImagePostResponse extends BasePostEntityResponse {
    private String description;
    private List<FileUploadResponse> imageUrls;

}