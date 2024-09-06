package com.avinash.danumalk.posts.image_post;

import com.avinash.danumalk.common.PageResponse;
import com.avinash.danumalk.common.ResultResponse;
import com.avinash.danumalk.exceptions.UnauthorizedAccessException;
import com.avinash.danumalk.exceptions.handleInvalidPostTypeException;
import com.avinash.danumalk.posts.PostTypeRepository;
import com.avinash.danumalk.user.User;
import com.avinash.danumalk.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
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
    private final PostTypeRepository postTypeRepository;


    @Override
    @Transactional
    public ResultResponse<ImagePostResponse> create(ImagePostRequest post, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        ImagePost imagePost = imagePostMapper.mapToImagePost(post);
        imagePost.setOwner(user);

        // Save and fetch the ImagePost with PostType
        ImagePost savedImagePost = imagePostRepository.saveAndFetchWithPostType(imagePost, post.postTypeId());

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
        savedImagePost.setPostType(savedImagePost.getPostType());

        // Save the updated imagePost once more if necessary
        savedImagePost = imagePostRepository.save(savedImagePost);

        // Create response and include only the valid images
        ImagePostResponse response = imagePostMapper.mapToImagePostResponse(savedImagePost);

        return ResultResponse.<ImagePostResponse>builder()
                .status("OK")
                .data(response)
                .build();
    }

    @Override
    @Transactional
    public ResultResponse<ImagePostResponse> update(UUID id, ImagePostRequest post) {
            ImagePost existingPost = imagePostRepository.findById(id).orElseThrow(() -> new IllegalStateException("Post not found!"));

            /* Get the user ID from the existing post */
            UUID postOwnerId = existingPost.getOwner().getId();

           /* Compare the post owner's ID with the authenticated user's ID */
            if(!postOwnerId.equals(securityUtils.getAuthenticatedUserId())){
                throw new IllegalStateException("You do not have permission to update this post!");
            }

            var postType = postTypeRepository.findById(post.postTypeId()).orElseThrow(() -> new IllegalStateException("Post Type not found!"));

            if (!postType.equals(existingPost.getPostType())){
                throw new handleInvalidPostTypeException("Cannot Change the post type");
            }


            /* Extract existing image URLs and new image URLs to separate lists */
            List<String> existingImageUrls = existingPost.getImageUrls();
            List<String> newImageUrls = post.imageUrls() != null ? post.imageUrls() : new ArrayList<>();

            /* List to store valid image URLs */
            List<String> validImageUrls = new ArrayList<>();

            /* Move new images from temp directory to post directory */
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

            /* Delete images from the post directory that are not in the new image URLs */
            if (post.imageUrls() != null) {
                for (String existingImageUrl : existingImageUrls) {
                    if (!newImageUrls.contains(existingImageUrl)) {
                        validImageUrls.remove(existingImageUrl);
                        imagePostHelper.deleteImageFromPostDirectory(existingImageUrl, existingPost.getId());
                    }
                }
            }

            /* Update the post details */
            ImagePost updatedPost = imagePostMapper.mapToImagePost(post);

            updatedPost.setOwner(existingPost.getOwner());
            updatedPost.setId(id); /* Ensure the ID remains the same */
            updatedPost.setImageUrls(validImageUrls);
            updatedPost.setPostType(postType);
            var savedImagePost = imagePostRepository.save(updatedPost);

            /* Create response */
            ImagePostResponse response = imagePostMapper.mapToImagePostResponse(savedImagePost);

            return ResultResponse.<ImagePostResponse>builder()
                    .status("OK")
                    .data(response)
                    .build();

    }

    @Override
    public boolean delete(UUID id) {
        ImagePost existingPost = imagePostRepository.findById(id).orElseThrow(() -> new IllegalStateException("Post not found!"));
        if (existingPost != null && existingPost.getOwner().getId().equals(securityUtils.getAuthenticatedUserId())) {

            //Delete the imageurl's from storage for the respective post which is deleted
            List<String> existingImageUrls = existingPost.getImageUrls();
            for(String imageUrl : existingImageUrls){
                imagePostHelper.deleteImageFromPostDirectory(imageUrl, existingPost.getId());
            }
            imagePostRepository.deleteById(id);
            return true;
        }else {
            throw new UnauthorizedAccessException("Unauthorized to delete");
        }

    }

    @Override
    public ResultResponse<ImagePostResponse> getById(UUID id) {
        ImagePost existingPost = imagePostRepository.findById(id).orElseThrow(() -> new IllegalStateException("Post not found!"));
        ImagePostResponse response = imagePostMapper.mapToImagePostResponse(existingPost);

        return ResultResponse.<ImagePostResponse>builder()
                .status("OK")
                .data(response)
                .build();
    }



    @Override
    public PageResponse<ImagePostResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<ImagePost> imagepostPage = imagePostRepository.findAllPosts(pageable);
        List<ImagePostResponse> imagePostResponses = imagepostPage.stream().map(imagePostMapper::mapToImagePostResponse).toList();

        return new PageResponse<>(
                imagePostResponses,
                imagepostPage.getNumber(),
                imagepostPage.getSize(),
                imagepostPage.getTotalElements(),
                imagepostPage.getTotalPages(),
                imagepostPage.isFirst(),
                imagepostPage.isLast()
        );
    }

}