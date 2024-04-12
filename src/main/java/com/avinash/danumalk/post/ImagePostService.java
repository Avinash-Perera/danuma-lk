package com.avinash.danumalk.post;

import com.avinash.danumalk.exceptions.handleInvalidPostTypeException;
import com.avinash.danumalk.exceptions.UnauthorizedAccessException;
import com.avinash.danumalk.util.SecurityUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class ImagePostService implements ImagePostServiceInterface {
    private final ImagePostRepository imagePostRepository;
    private final ImagePostMapper imagePostMapper; // Add the ImagePostMapper
    private final SecurityUtils securityUtils;
    @Override
    public List<ImagePostDTO> getAllImagePosts() {
        var imagePosts = imagePostRepository.findAll();
        return Collections.singletonList(imagePostMapper.imagePostToDTO((ImagePost) imagePosts)); // Use the mapper to convert to DTOs
    }
    @Override
    public ImagePostDTO getImagePostById(Long imagePostId) {
        var imagePost = imagePostRepository.findById(imagePostId).orElse(null);
        if (imagePost != null) {
            return imagePostMapper.imagePostToDTO(imagePost); // Use the mapper to convert to DTO
        }
        return null;
    }
    @Override
    public ImagePostDTO createImagePost(ImagePostDTO imagePostDTO) {

        if (!imagePostDTO.getPostType().equals(PostType.IMAGE)) {
            throw new IllegalArgumentException("Invalid post type for ImagePost.");
        }
        // setting authenticated users id
        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();
        System.out.println(authenticatedUserId);
        imagePostDTO.setUserId(authenticatedUserId);
        var imagePost = imagePostMapper.dtoToImagePost(imagePostDTO);
        imagePost.getUser().setId(authenticatedUserId);
        var savedImagePost = imagePostRepository.save(imagePost);
        return imagePostMapper.imagePostToDTO(savedImagePost);
    }
    @Override
    public ImagePostDTO updateImagePost(Long imagePostId, ImagePostDTO updatedImagePostDTO) {
        if (updatedImagePostDTO.getPostType() != PostType.IMAGE) {
            throw new handleInvalidPostTypeException("Cannot change the post type for ImagePost.");
        }
        var existingImagePost = imagePostRepository.findById(imagePostId).orElse(null);
        if (existingImagePost == null) {
            throw new IllegalArgumentException("Image post not found.");
        }
        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();
        // Check if the authenticated user created the post
        if (!existingImagePost.getUser().getId().equals(authenticatedUserId)) {
            throw new UnauthorizedAccessException("Unauthorized to update this image post.");
        }
        updatedImagePostDTO.setUserId(authenticatedUserId);
        var imagePost = imagePostMapper.dtoToImagePost(updatedImagePostDTO);
        imagePost.setPostId(imagePostId);
        imagePost.getUser().setId(authenticatedUserId);
        var savedImagePost = imagePostRepository.save(imagePost);
        return imagePostMapper.imagePostToDTO(savedImagePost);

    }
    @Override
    public boolean deleteImagePost(Long imagePostId) {
        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();
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