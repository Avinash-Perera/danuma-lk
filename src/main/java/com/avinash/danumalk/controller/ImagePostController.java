package com.avinash.danumalk.controller;

import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.ImagePost;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.service.ImagePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/image")
@CrossOrigin
public class ImagePostController {
    @Autowired
    private ImagePostService imagePostService;


//Create Image post
    @PostMapping
    public ImagePost createImagePost(@RequestBody ImagePost imagePost) {
        if (imagePost.getPostType() != PostType.IMAGE) {
            throw new IllegalArgumentException("Invalid post type for ImagePost.");
        }
        return imagePostService.createImagePost(imagePost);
    }

    // Update ImagePost by ID
    @PutMapping("/{imagePostId}")
    public ImagePost updateImagePost(@PathVariable Long imagePostId, @RequestBody ImagePost updatedImagePost) {
        ImagePost existingImagePost = imagePostService.getImagePostById(imagePostId);
        if (existingImagePost != null) {
            // Check if the provided ID matches the post type
            if (existingImagePost.getPostType() != PostType.IMAGE) {
                throw new IllegalArgumentException("Invalid post type for ImagePost.");
            }

            // Check if the PostType in the updated post matches the existing PostType
            if (updatedImagePost.getPostType() != PostType.IMAGE) {
                throw new IllegalArgumentException("Cannot change the post type for ImagePost.");
            }
            // Clear the existing comments from the database (using orphanRemoval)
            existingImagePost.getComments().clear();

            updatedImagePost.setPostId(imagePostId);

            // Update other properties of the existingTextPost as needed
            existingImagePost.setTitle(updatedImagePost.getTitle());
            existingImagePost.setImageDescription(updatedImagePost.getImageDescription());

            // Add the updated comments to the existingTextPost
            List<Comment> updatedComments = updatedImagePost.getComments();
            if (updatedComments != null) {
                for (Comment comment : updatedComments) {
                    comment.setPost(existingImagePost); // Set the parent post
                    existingImagePost.getComments().add(comment); // Add the comment
                }
            }

            return imagePostService.updateImagePost(imagePostId, updatedImagePost);
        }
        return null; // ImagePost not found
    }

    // Delete ImagePost by ID
    @DeleteMapping("/{imagePostId}")
    public boolean deleteImagePost(@PathVariable Long imagePostId) {
        ImagePost existingImagePost = imagePostService.getImagePostById(imagePostId);
        if (existingImagePost != null) {
            // Check if the provided ID matches the post type
            if (existingImagePost.getPostType() != PostType.IMAGE) {
                throw new IllegalArgumentException("Invalid post type for ImagePost.");
            }
            return imagePostService.deleteImagePost(imagePostId);
        }
        return false; // ImagePost not found
    }
}
