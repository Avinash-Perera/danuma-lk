package com.avinash.danumalk.service;

import com.avinash.danumalk.model.VideoPost;
import com.avinash.danumalk.repository.VideoPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoPostService {
    @Autowired
    private PostService postService;
    @Autowired
    private VideoPostRepository videoPostRepository;

    public List<VideoPost> getAllVideoPosts() {
        return videoPostRepository.findAll();
    }

    public VideoPost getVideoPostById(Long videoPostId) {
        return videoPostRepository.findById(videoPostId).orElse(null);
    }

    public VideoPost createVideoPost(VideoPost videoPost) {
        return (VideoPost) postService.createPost(videoPost);
    }

    public VideoPost updateVideoPost(Long videoPostId, VideoPost updatedVideoPost) {
        if (videoPostRepository.existsById(videoPostId)) {
            updatedVideoPost.setPostId(videoPostId);
            VideoPost savedVideoPost = videoPostRepository.save(updatedVideoPost);
            // Update the post in the main post table and return the updated object
            return (VideoPost) postService.updatePost(videoPostId, savedVideoPost);
        }
        return null; // VideoPost not found
    }

    public boolean deleteVideoPost(Long videoPostId) {
        // Call the common deletePost method from PostService
        return postService.deletePost(videoPostId);
    }
}
