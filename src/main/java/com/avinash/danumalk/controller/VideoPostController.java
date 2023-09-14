package com.avinash.danumalk.controller;

import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.model.VideoPost;
import com.avinash.danumalk.service.VideoPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/video")
@CrossOrigin
public class VideoPostController {
    @Autowired
    private VideoPostService videoPostService;

    //Create Video post
    @PostMapping
    public VideoPost createVideoPost(@RequestBody VideoPost videoPost) {
        if (videoPost.getPostType() != PostType.VIDEO) {
            throw new IllegalArgumentException("Invalid post type for VideoPost.");
        }
        return videoPostService.createVideoPost(videoPost);
    }

    // Update VideoPost by ID
    @PutMapping("/{videoPostId}")
    public VideoPost updateVideoPost(@PathVariable Long videoPostId, @RequestBody VideoPost updatedVideoPost) {
        VideoPost existingVideoPost = videoPostService.getVideoPostById(videoPostId);
        if (existingVideoPost != null) {
            // Check if the provided ID matches the post type
            if (existingVideoPost.getPostType() != PostType.VIDEO) {
                throw new IllegalArgumentException("Invalid post type for VideoPost.");
            }

            // Check if the PostType in the updated post matches the existing PostType
            if (updatedVideoPost.getPostType() != PostType.VIDEO) {
                throw new IllegalArgumentException("Cannot change the post type for VideoPost.");
            }

            // Clear the existing comments from the database (using orphanRemoval)
            existingVideoPost.getComments().clear();

            updatedVideoPost.setPostId(videoPostId);

            // Update other properties of the existingTextPost as needed
            existingVideoPost.setTitle(updatedVideoPost.getTitle());
            existingVideoPost.setVideoDescription(updatedVideoPost.getVideoDescription());

            // Add the updated comments to the existingTextPost
            List<Comment> updatedComments = updatedVideoPost.getComments();
            if (updatedComments != null) {
                for (Comment comment : updatedComments) {
                    comment.setPost(existingVideoPost); // Set the parent post
                    existingVideoPost.getComments().add(comment); // Add the comment
                }
            }

            return videoPostService.updateVideoPost(videoPostId, updatedVideoPost);
        }
        return null; // VideoPost not found
    }

    // Delete VideoPost by ID
    @DeleteMapping("/{videoPostId}")
    public boolean deleteVideoPost(@PathVariable Long videoPostId) {
        VideoPost existingVideoPost = videoPostService.getVideoPostById(videoPostId);
        if (existingVideoPost != null) {
            // Check if the provided ID matches the post type
            if (existingVideoPost.getPostType() != PostType.VIDEO) {
                throw new IllegalArgumentException("Invalid post type for VideoPost.");
            }
            return videoPostService.deleteVideoPost(videoPostId);
        }
        return false; // VideoPost not found
    }
}
