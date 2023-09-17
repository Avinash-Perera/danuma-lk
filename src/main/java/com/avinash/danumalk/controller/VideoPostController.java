package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.VideoPostDTO;
import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.model.VideoPost;
import com.avinash.danumalk.service.VideoPostService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/video")
@CrossOrigin
@AllArgsConstructor
public class VideoPostController {
    private final VideoPostService videoPostService;

    //Create Video post
    @PostMapping
    public VideoPostDTO createVideoPost(@RequestBody VideoPostDTO videoPostDTO) {
        if (videoPostDTO.getPostType() != PostType.VIDEO) {
            throw new IllegalArgumentException("Invalid post type for VideoPost.");
        }
        return videoPostService.createVideoPost(videoPostDTO);
    }

    // Update VideoPost by ID
    @PutMapping("/{videoPostId}")
    public VideoPostDTO updateVideoPost(@PathVariable Long videoPostId, @RequestBody VideoPostDTO updatedVideoPostDTO) {
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

    // Delete VideoPost by ID
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
