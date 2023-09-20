package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.VideoPostDTO;
import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.model.VideoPost;
import com.avinash.danumalk.service.VideoPostService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/video")
@CrossOrigin
@AllArgsConstructor
@Validated // Enable validation for this controller
public class VideoPostController {
    private final VideoPostService videoPostService;

    /**
     * Create a video post.
     *
     * @param videoPostDTO The video post data transfer object.
     * @return The created video post data transfer object.
     * @throws IllegalArgumentException if the post type is invalid.
     */
    @PostMapping
    public VideoPostDTO createVideoPost(@RequestBody @Valid VideoPostDTO videoPostDTO) {
        if (!videoPostDTO.getPostType().equals(PostType.VIDEO)) {
            throw new IllegalArgumentException("Invalid post type for VideoPost.");
        }
        return videoPostService.createVideoPost(videoPostDTO);
    }

    /**
     * Updates a VideoPost by its ID.
     *
     * @param videoPostId The ID of the VideoPost to update.
     * @param updatedVideoPostDTO The updated VideoPostDTO containing the new data.
     * @return The updated VideoPostDTO.
     * @throws IllegalArgumentException If the provided ID does not match the post type
     *                                  or if the post type is changed.
     */
    @PutMapping("/{videoPostId}")
    public VideoPostDTO updateVideoPost(@PathVariable Long videoPostId, @RequestBody @Valid  VideoPostDTO updatedVideoPostDTO) {
        VideoPostDTO existingVideoPostDTO = videoPostService.getVideoPostById(videoPostId);
        if (existingVideoPostDTO != null) {
            // Check if the provided ID matches the post type
            if (existingVideoPostDTO.getPostType() != PostType.VIDEO) {
                throw new IllegalArgumentException("Invalid post type for VideoPost.");
            }

            // Check if the PostType in the updated post matches the existing PostType
            if (updatedVideoPostDTO.getPostType() != PostType.VIDEO) {
                throw new IllegalArgumentException("Cannot change the post type for VideoPost.");
            }
            // Update other properties of the existingVideoPostDTO as needed
            existingVideoPostDTO.setTitle(updatedVideoPostDTO.getTitle());
            existingVideoPostDTO.setVideoUrl(updatedVideoPostDTO.getVideoUrl());
            existingVideoPostDTO.setVideoDescription(updatedVideoPostDTO.getVideoDescription());

            return videoPostService.updateVideoPost(videoPostId, updatedVideoPostDTO);
        }
        return null; // VideoPost not found
    }

    /**
     * Deletes a video post by its ID.
     *
     * @param  videoPostId  the ID of the video post to be deleted
     * @return              true if the video post was successfully deleted, false otherwise
     */
    @DeleteMapping("/{videoPostId}")
    public boolean deleteVideoPost(@PathVariable Long videoPostId) {
        VideoPostDTO existingVideoPostDTO = videoPostService.getVideoPostById(videoPostId);
        if (existingVideoPostDTO != null) {
            // Check if the provided ID matches the post type
            if (existingVideoPostDTO.getPostType() != PostType.VIDEO) {
                throw new IllegalArgumentException("Invalid post type for VideoPost.");
            }
            return videoPostService.deleteVideoPost(videoPostId);
        }
        return false; // VideoPost not found
    }
}
