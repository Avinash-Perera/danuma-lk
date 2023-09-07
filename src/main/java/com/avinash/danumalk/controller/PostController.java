package com.avinash.danumalk.controller;

import com.avinash.danumalk.model.*;
import com.avinash.danumalk.service.ImagePostService;
import com.avinash.danumalk.service.PostService;
import com.avinash.danumalk.service.TextPostService;
import com.avinash.danumalk.service.VideoPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private ImagePostService imagePostService;

    @Autowired
    private TextPostService textPostService;

    @Autowired
    private VideoPostService videoPostService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<Post> getPostById(@PathVariable Long postId) {
        Post post = postService.getPostById(postId);

        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    //Creating New Posts
  @PostMapping("/text")
    public Post createPost(@RequestBody TextPost textPost) {
        if (textPost.getPostType() != PostType.TEXT) {
            throw new IllegalArgumentException("Invalid post type for TextPost.");
        }
        return textPostService.createTextPost(textPost);
    }
    @PostMapping("/image")
    public ImagePost createImagePost(@RequestBody ImagePost imagePost) {
        if (imagePost.getPostType() != PostType.IMAGE) {
            throw new IllegalArgumentException("Invalid post type for ImagePost.");
        }
        return imagePostService.createImagePost(imagePost);
    }
    @PostMapping("/video")
    public VideoPost createVideoPost(@RequestBody VideoPost videoPost) {
        if (videoPost.getPostType() != PostType.VIDEO) {
            throw new IllegalArgumentException("Invalid post type for VideoPost.");
        }
        return videoPostService.createVideoPost(videoPost);
    }

    // Update TextPost by ID
    @PutMapping("/text/{textPostId}")
    public TextPost updateTextPost(@PathVariable Long textPostId, @RequestBody TextPost updatedTextPost) {
        TextPost existingTextPost = textPostService.getTextPostById(textPostId);
        if (existingTextPost != null) {
            // Check if the provided ID matches the post type
            if (existingTextPost.getPostType() != PostType.TEXT) {
                throw new IllegalArgumentException("Invalid post type for TextPost.");
            }

            // Check if the PostType in the updated post matches the existing PostType
            if (updatedTextPost.getPostType() != PostType.TEXT) {
                throw new IllegalArgumentException("Cannot change the post type for TextPost.");
            }

            updatedTextPost.setPostId(textPostId);
            return textPostService.updateTextPost(textPostId, updatedTextPost);
        }
        return null; // TextPost not found
    }

    // Update ImagePost by ID
    @PutMapping("/image/{imagePostId}")
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

            updatedImagePost.setPostId(imagePostId);
            return imagePostService.updateImagePost(imagePostId, updatedImagePost);
        }
        return null; // ImagePost not found
    }

    // Update VideoPost by ID
    @PutMapping("/video/{videoPostId}")
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

            updatedVideoPost.setPostId(videoPostId);
            return videoPostService.updateVideoPost(videoPostId, updatedVideoPost);
        }
        return null; // VideoPost not found
    }

    // Delete TextPost by ID
    @DeleteMapping("/text/{textPostId}")
    public boolean deleteTextPost(@PathVariable Long textPostId) {
        TextPost existingTextPost = textPostService.getTextPostById(textPostId);
        if (existingTextPost != null) {
            // Check if the provided ID matches the post type
            if (existingTextPost.getPostType() != PostType.TEXT) {
                throw new IllegalArgumentException("Invalid post type for TextPost.");
            }
            return textPostService.deleteTextPost(textPostId);
        }
        return false; // TextPost not found
    }

    // Delete ImagePost by ID
    @DeleteMapping("/image/{imagePostId}")
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

    // Delete VideoPost by ID
    @DeleteMapping("/video/{videoPostId}")
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
