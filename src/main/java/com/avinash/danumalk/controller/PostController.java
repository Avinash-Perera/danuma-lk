package com.avinash.danumalk.controller;

import com.avinash.danumalk.model.*;
import com.avinash.danumalk.service.ImagePostService;
import com.avinash.danumalk.service.PostService;
import com.avinash.danumalk.service.TextPostService;
import com.avinash.danumalk.service.VideoPostService;
import org.springframework.beans.factory.annotation.Autowired;
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
}
