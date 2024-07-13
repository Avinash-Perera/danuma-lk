package com.avinash.danumalk.posts;

import com.avinash.danumalk.file.FileUploadResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ImagePostMapper {
    private final ImagePostHelper imagePostHelper;


    public ImagePost mapToImagePost(ImagePostRequest request) {
        return ImagePost.builder()
                .title(request.title())
                .description(request.description())
                .imageUrls(request.imageUrls())
                .build();
    }

    public ImagePostResponse mapToImagePostResponse(ImagePost post) {
        List<FileUploadResponse> fileUploadResponses = post.getImageUrls().stream()
                .map(imageUrl -> {
                    if (imageUrl.isEmpty()) {
                        return new FileUploadResponse("", "");
                    } else {
                        return new FileUploadResponse(imageUrl, imagePostHelper.getImageUrl(imageUrl, post.getId()));
                    }
                })
                .collect(Collectors.toList());

        return ImagePostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .description(post.getDescription())
                .imageUrls(fileUploadResponses)
                .build();
    }
}
