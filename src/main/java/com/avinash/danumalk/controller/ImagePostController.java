package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.ImagePostDTO;
import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.ImagePost;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.service.ImagePostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/image")
@CrossOrigin
@AllArgsConstructor
public class ImagePostController {
    private final ImagePostService imagePostService;


    //Create Image post
    @PostMapping
    public ImagePostDTO createImagePost(@RequestBody ImagePostDTO imagePostDTO) {
        if (imagePostDTO.getPostType() != PostType.IMAGE) {
            throw new IllegalArgumentException("Invalid post type for TextPost.");
        }
        return imagePostService.createImagePost(imagePostDTO);
    }


    // Update ImagePost by ID
    @PutMapping("/{imagePostId}")
    public ImagePostDTO updateImagePost(@PathVariable Long imagePostId, @RequestBody ImagePostDTO updatedImagePostDTO) {
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

    // Delete ImagePost by ID
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
