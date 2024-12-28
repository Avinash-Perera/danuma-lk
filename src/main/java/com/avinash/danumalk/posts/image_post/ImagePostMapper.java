package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.file.FileUploadResponse;
import com.avinash.danumalk.posts.*;
import com.avinash.danumalk.user.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ImagePostMapper {
    private final ImagePostHelper imagePostHelper;
    private final PostTypeMapper postTypeMapper;
    private final PostCategoryMapper postCategoryMapper;


    public ImagePost mapToImagePost(ImagePostRequest request) {
        return ImagePost.builder()
                .description(request.description())
                .imageUrls(request.imageUrls())
                .build();
    }


    public ImagePostResponse mapToImagePostResponse(ImagePost ipost, long reactionCount, long commentCount, Boolean isUserReacted) {
        // Map image URLs to FileUploadResponse objects
        List<FileUploadResponse> fileUploadResponses = ipost.getImageUrls().stream()
                .map(imageUrl -> new FileUploadResponse(imageUrl, imagePostHelper.getImageUrl(imageUrl, ipost.getId())))
                .collect(Collectors.toList());

        // Map PostType to PostTypeResponse
        PostTypeResponse postTypeResponse = postTypeMapper.mapToPostTypeResponse(ipost.getPostType());

        // Map PostCategories to a list of PostCategoryResponse
        List<PostCategory> categories = ipost.getCategories();
        List<PostCategoryResponse> categoryResponses = new ArrayList<>();

        for (PostCategory category : categories) {
            PostCategoryResponse response = postCategoryMapper.mapToPostCategoryResponse(category);
            categoryResponses.add(response);
        }

        // Assuming 'users_name' is available in ImagePost. If not, you need to set it somewhere
        String usersName = ipost.getOwner().getEmail();
        Boolean ownerAccountStatus = ipost.getOwner().isAccountLocked();

        // Build and return the ImagePostResponse
        return ImagePostResponse.builder()
                .id(ipost.getId())
                .description(ipost.getDescription())
                .imageUrls(fileUploadResponses)
                .postTypeResponse(postTypeResponse)
                .categories(categoryResponses)
                .postOwner(usersName)
                .reactionCount(reactionCount)
                .commentCount(commentCount)
                .createdDate(String.valueOf(ipost.getCreatedDate()))
                .isUserReacted(isUserReacted)
                .isOwnerLocked(ownerAccountStatus)
                .build();
    }


}





//    public ImagePostResponse mapToImagePostResponse(ImagePost ipost) {
//        User currentUser = getCurrentUser();
//        List<FileUploadResponse> fileUploadResponses = ipost.getImageUrls().stream()
//                .map(imageUrl -> new FileUploadResponse(imageUrl, imagePostHelper.getImageUrl(imageUrl, ipost.getId())))
//                .collect(Collectors.toList());
//
//        PostTypeResponse postTypeResponse = postTypeMapper.mapToPostTypeResponse(ipost.getPostType());
//
//        // Assuming 'users_name' is available in ImagePost. If not, you need to set it somewhere
//        String usersName = ipost.getOwner().getEmail();
//
//        long reactionCount = ipost.getReactions().size();
//
//        boolean isUserReacted = ipost.getReactions().stream()
//                .anyMatch(reaction -> reaction.getOwner().getId().equals(currentUser.getId()));
//
//
//        return ImagePostResponse.builder()
//                .id(ipost.getId())
//                .description(ipost.getDescription())
//                .imageUrls(fileUploadResponses)
//                .postTypeResponse(postTypeResponse)
//                .postOwner(usersName)
//                .reactionCount(reactionCount)
//                .isUserReacted(isUserReacted)
//                .build();
//    }