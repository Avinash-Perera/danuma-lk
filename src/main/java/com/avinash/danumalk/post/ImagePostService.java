package com.avinash.danumalk.post;

import com.avinash.danumalk.auth.AuthenticationService;
import com.avinash.danumalk.exceptions.handleInvalidPostTypeException;
import com.avinash.danumalk.exceptions.UnauthorizedAccessException;
import com.avinash.danumalk.user.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ImagePostService {
    private final ImagePostRepository imagePostRepository;
    private final ImagePostMapper imagePostMapper; // Add the ImagePostMapper
    private final AuthenticationService authenticationService;


    public List<ImagePostDTO> getAllImagePosts() {
        var imagePosts = imagePostRepository.findAll();
        return Collections.singletonList(imagePostMapper.imagePostToDTO((ImagePost) imagePosts)); // Use the mapper to convert to DTOs
    }

    public ImagePostDTO getImagePostById(Long imagePostId) {
        var imagePost = imagePostRepository.findById(imagePostId).orElse(null);
        if (imagePost != null) {
            return imagePostMapper.imagePostToDTO(imagePost); // Use the mapper to convert to DTO
        }
        return null;
    }

    public ImagePostDTO createImagePost(ImagePostDTO imagePostDTO) {
        // Check if the post type is valid
        if (!imagePostDTO.getPostType().equals(PostType.IMAGE)) {
            throw new IllegalArgumentException("Invalid post type for ImagePost.");
        }

        // Get the authenticated user's ID
        Integer userId = authenticationService.getAuthenticatedUserId();
        // Set the user ID in the ImagePostDTO
        imagePostDTO.setUserId(userId);

        // Convert ImagePostDTO to ImagePost entity using builder pattern
        var imagePost = ImagePost.builder()
                .title(imagePostDTO.getTitle())
                .postType(imagePostDTO.getPostType())
                .user(User.builder().id(imagePostDTO.getUserId()).build())
                .imageUrl(imagePostDTO.getImageUrl())
                .imageDescription(imagePostDTO.getImageDescription())
                .build();

        // Save the image post
        ImagePost savedImagePost = imagePostRepository.save(imagePost);

        // Convert ImagePost entity back to ImagePostDTO
        return imagePostMapper.imagePostToDTO(savedImagePost);
    }


    public ImagePostDTO updateImagePost(Long imagePostId, ImagePostDTO updatedImagePostDTO) {

        if (updatedImagePostDTO.getPostType() != PostType.IMAGE) {
            throw new handleInvalidPostTypeException("Cannot change the post type for ImagePost.");
        }

        // Retrieve the existing ImagePost from the repository
        var existingImagePost = imagePostRepository.findById(imagePostId).orElse(null);


        // Check if the existing ImagePost exists
        if (existingImagePost == null) {
            throw new IllegalArgumentException("Image post not found.");
        }

        // Retrieve the authenticated user ID
        Integer authenticatedUserId = authenticationService.getAuthenticatedUserId();

        // Check if the authenticated user created the post
        if (!existingImagePost.getUser().getId().equals(authenticatedUserId)) {
            throw new UnauthorizedAccessException("Unauthorized to update this image post.");
        }

        // Update properties of the existing ImagePost entity with the values from the updated ImagePost DTO
        existingImagePost.setTitle(updatedImagePostDTO.getTitle());
        existingImagePost.setImageUrl(updatedImagePostDTO.getImageUrl());
        existingImagePost.setImageDescription(updatedImagePostDTO.getImageDescription());

        // Save the updated ImagePost entity
        var savedImagePost = imagePostRepository.save(existingImagePost);
        System.out.println(savedImagePost);

        // Convert the updated ImagePost entity back to DTO and return
        return imagePostMapper.imagePostToDTO(savedImagePost);
    }



    public boolean deleteImagePost(Long imagePostId) {
        Integer authenticatedUserId = authenticationService.getAuthenticatedUserId();

        // Check if the authenticated user created the post
        var existingImagePost = imagePostRepository.findById(imagePostId).orElse(null);
        if (existingImagePost != null && existingImagePost.getUser().getId().equals(authenticatedUserId)) {
            // Check if the provided ID matches the post type
            if (existingImagePost.getPostType() != PostType.IMAGE) {
                throw new IllegalArgumentException("Invalid post type for ImagePost.");
            }

            imagePostRepository.deleteById(imagePostId);
            return true;
        }else {
                throw new UnauthorizedAccessException("Unauthorized to delete this image post or post not found.");
            }

    }

}