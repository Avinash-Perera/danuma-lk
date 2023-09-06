package com.avinash.danumalk.service;

import com.avinash.danumalk.model.VideoPost;
import com.avinash.danumalk.repository.VideoPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoPostService {

    @Autowired
    private VideoPostRepository videoPostRepository;

    public List<VideoPost> getAllVideoPosts() {
        return videoPostRepository.findAll();
    }

    public VideoPost getVideoPostById(Long videoPostId) {
        return videoPostRepository.findById(videoPostId).orElse(null);
    }

    public VideoPost createVideoPost(VideoPost videoPost) {
        return videoPostRepository.save(videoPost);
    }

    public VideoPost updateVideoPost(Long videoPostId, VideoPost updatedVideoPost) {
        if (videoPostRepository.existsById(videoPostId)) {
            updatedVideoPost.setPostId(videoPostId);
            return videoPostRepository.save(updatedVideoPost);
        }
        return null; // VideoPost not found
    }

    public boolean deleteVideoPost(Long videoPostId) {
        if (videoPostRepository.existsById(videoPostId)) {
            videoPostRepository.deleteById(videoPostId);
            return true;
        }
        return false; // VideoPost not found
    }
}
