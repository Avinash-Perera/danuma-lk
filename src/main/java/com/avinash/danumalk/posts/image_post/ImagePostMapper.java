package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.file.FileUploadResponse;
import com.avinash.danumalk.posts.PostTypeMapper;
import com.avinash.danumalk.posts.PostTypeResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ImagePostMapper {
    private final ImagePostHelper imagePostHelper;
    private final PostTypeMapper postTypeMapper;

    public ImagePost mapToImagePost(ImagePostRequest request) {
        return ImagePost.builder()
                .description(request.description())
                .imageUrls(request.imageUrls())
                .build();
    }

    public ImagePostResponse mapToImagePostResponse(ImagePost ipost) {
        List<FileUploadResponse> fileUploadResponses = ipost.getImageUrls().stream()
                .map(imageUrl -> new FileUploadResponse(imageUrl, imagePostHelper.getImageUrl(imageUrl, ipost.getId())))
                .collect(Collectors.toList());


        PostTypeResponse postTypeResponse = postTypeMapper.mapToPostTypeResponse(ipost.getPostType());

        // Assuming 'users_name' is available in ImagePost. If not, you need to set it somewhere
        String usersName = ipost.getOwner().getEmail();

        return ImagePostResponse.builder()
                .id(ipost.getId())
                .description(ipost.getDescription())
                .imageUrls(fileUploadResponses)
                .postTypeResponse(postTypeResponse)
                .postOwner(usersName)
                .build();
    }
}
