package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.ImagePostDTO;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.service.ImagePostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/image")
@CrossOrigin
@AllArgsConstructor
@Validated // Enable validation for this controller
public class ImagePostController {
    private final ImagePostService imagePostService;


    /**
     * Creates an image post.
     *
     * @param  imagePostDTO  the image post DTO containing the details of the image post
     * @return               the created image post DTO
     */
    @PostMapping
    public ImagePostDTO createImagePost(@RequestBody @Valid ImagePostDTO imagePostDTO) {
        if (!imagePostDTO.getPostType().equals(PostType.IMAGE)) {
            throw new IllegalArgumentException("Invalid post type for TextPost.");
        }
        return imagePostService.createImagePost(imagePostDTO);
    }


    /**
     * Updates an ImagePost with the provided imagePostId and updatedImagePostDTO.
     *
     * @param  imagePostId              the ID of the ImagePost to be updated
     * @param  updatedImagePostDTO      the updated ImagePostDTO object containing new values
     * @return                          the updated ImagePostDTO object
     */
    @PutMapping("/{imagePostId}")
    public ImagePostDTO updateImagePost(@PathVariable Long imagePostId, @RequestBody @Valid  ImagePostDTO updatedImagePostDTO) {
        ImagePostDTO existingImagePostDTO = imagePostService.getImagePostById(imagePostId);
        if (existingImagePostDTO != null) {
            // Check if the provided ID matches the post type
            if (existingImagePostDTO.getPostType() != PostType.IMAGE) {
                throw new IllegalArgumentException("Invalid post type for ImagePost.");
            }
            // Check if the PostType in the updated post matches the existing PostType
            if (updatedImagePostDTO.getPostType() != PostType.IMAGE) {
                throw new IllegalArgumentException("Cannot change the post type for ImagePost.");
            }
            // Update other properties of the existingImagePostDTO as needed
            existingImagePostDTO.setTitle(updatedImagePostDTO.getTitle());
            existingImagePostDTO.setImageUrl(updatedImagePostDTO.getImageUrl());
            existingImagePostDTO.setImageDescription(updatedImagePostDTO.getImageDescription());

            return imagePostService.updateImagePost(imagePostId, updatedImagePostDTO);
        }
        return null; // ImagePost not found
    }


    /**
     * Deletes an image post by its ID.
     *
     * @param  imagePostId  the ID of the image post to be deleted
     * @return              true if the image post was successfully deleted, false otherwise
     */
    @DeleteMapping("/{imagePostId}")
    public boolean deleteImagePost(@PathVariable Long imagePostId) {
        ImagePostDTO existingImagePostDTO = imagePostService.getImagePostById(imagePostId);
        if (existingImagePostDTO != null) {
            // Check if the provided ID matches the post type
            if (existingImagePostDTO.getPostType() != PostType.IMAGE) {
                throw new IllegalArgumentException("Invalid post type for ImagePost.");
            }
            return imagePostService.deleteImagePost(imagePostId);
        }
        return false; // ImagePost not found
    }
}
