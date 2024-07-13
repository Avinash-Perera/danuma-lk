package com.avinash.danumalk.posts;

import com.avinash.danumalk.common.ResultResponse;
import com.avinash.danumalk.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImagePostService implements InterfaceImagePostService {
    private final SecurityUtils securityUtils;
    private final ImagePostMapper imagePostMapper;
    private final ImagePostRepository imagePostRepository;
    private final ImagePostHelper imagePostHelper;
    private final ImagePostUtils imagePostUtils;


    @Override
    public ResultResponse<ImagePostResponse> create(ImagePostRequest post) {

        ImagePost imagePost = imagePostMapper.mapToImagePost(post);
        // In memory user assigning method
        var owner = imagePostUtils.initializeOwner(securityUtils.getAuthenticatedUserId());
        imagePost.setOwner(owner);

        // Save the ImagePost
        ImagePost savedImagePost = imagePostRepository.save(imagePost);

        List<String> validImageUrls = new ArrayList<>();

        // Move images from temp directory to post directory
        if (post.imageUrls() != null && !post.imageUrls().isEmpty()) {
            for (String imageUrl : post.imageUrls()) {
                if (imagePostHelper.isImageInTempDirectory(imageUrl)) {
                    imagePostHelper.movePostImageToPostFolder(imageUrl, savedImagePost.getId());
                    validImageUrls.add(imageUrl);
                } else {
                    log.warn("Image not found on server: {}", imageUrl);
                }
            }
        }

        // Update the imagePost with the valid image URLs
        savedImagePost.setImageUrls(validImageUrls);
        imagePostRepository.save(savedImagePost);

        // Create response and include only the valid images
        ImagePostResponse response = imagePostMapper.mapToImagePostResponse(savedImagePost);

        return ResultResponse.<ImagePostResponse>builder()
                .status("OK")
                .data(response)
                .build();
    }

//    // Move images from temp directory to post directory
//        if (post.imageUrls() != null && !post.imageUrls().isEmpty()) {
//        for (int i = 0; i < post.imageUrls().size(); i++) {
//            String imageUrl = post.imageUrls().get(i);
//            if (imagePostHelper.isImageInTempDirectory(imageUrl)) {
//                imagePostHelper.movePostImageToPostFolder(imageUrl, savedImagePost.getId());
//            } else {
//                log.warn("Image not found on server: {}", imageUrl);
//                // Set image URL to empty string for files not found
//                post.imageUrls().set(i, "");
//            }
//        }
//    }
    @Override
    public ResultResponse<ImagePostResponse> update(UUID id, ImagePostRequest post) {
            ImagePost existingPost = imagePostRepository.findById(id).orElseThrow(() -> new IllegalStateException("Post not found!"));

            // Get the user ID from the existing post
            UUID postOwnerId = existingPost.getOwner().getId();
            System.out.println(securityUtils.getAuthenticatedUserId());
            // Compare the post owner's ID with the authenticated user's ID
            if(!postOwnerId.equals(securityUtils.getAuthenticatedUserId())){
                throw new IllegalStateException("You do not have permission to update this post!");
            }



            // Extract existing image URLs and new image URLs to separate lists
            List<String> existingImageUrls = existingPost.getImageUrls();
            List<String> newImageUrls = post.imageUrls() != null ? post.imageUrls() : new ArrayList<>();

            // List to store valid image URLs
            List<String> validImageUrls = new ArrayList<>();

            // Move new images from temp directory to post directory
            for (String imageUrl : newImageUrls) {
                if (existingImageUrls.contains(imageUrl) || imagePostHelper.isImageInTempDirectory(imageUrl)) {
                    if (!existingImageUrls.contains(imageUrl)) {
                        imagePostHelper.movePostImageToPostFolder(imageUrl, id);
                    }
                    validImageUrls.add(imageUrl);
                } else {
                    log.warn("Image not found on server: {}", imageUrl);
                }
            }

            // Delete images from the post directory that are not in the new image URLs
            if (post.imageUrls() != null) {
                for (String existingImageUrl : existingImageUrls) {
                    if (!newImageUrls.contains(existingImageUrl)) {
                        validImageUrls.remove(existingImageUrl);
                        imagePostHelper.deleteImageFromPostDirectory(existingImageUrl, existingPost.getId());
                    }
                }
            }

            // Update the post details
            ImagePost updatedPost = imagePostMapper.mapToImagePost(post);

            updatedPost.setOwner(existingPost.getOwner());
            updatedPost.setId(id); // Ensure the ID remains the same
            updatedPost.setImageUrls(validImageUrls);
            var savedImagePost = imagePostRepository.save(updatedPost);
            imagePostRepository.save(savedImagePost);

            // Create response
            ImagePostResponse response = imagePostMapper.mapToImagePostResponse(savedImagePost);

            return ResultResponse.<ImagePostResponse>builder()
                    .status("OK")
                    .data(response)
                    .build();

    }

    @Override
    public void delete(UUID id) {

    }

    @Override
    public ImagePost getById(UUID id) {
        return null;
    }

    @Override
    public List<ImagePost> getAll() {
        return List.of();
    }


}
