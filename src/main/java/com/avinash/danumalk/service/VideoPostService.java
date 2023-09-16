package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.VideoPostDTO;
import com.avinash.danumalk.dto.VideoPostMapper;
import com.avinash.danumalk.model.VideoPost;
import com.avinash.danumalk.repository.VideoPostRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class VideoPostService {
    private final VideoPostRepository videoPostRepository;
    private final VideoPostMapper videoPostMapper; // Add the VideoPostMapper

    public List<VideoPostDTO> getAllVideoPosts() {
        List<VideoPost> videoPosts = videoPostRepository.findAll();
        return Collections.singletonList(videoPostMapper.videoPostToDTO((VideoPost) videoPosts)); // Use the mapper to convert to DTOs
    }

    public VideoPostDTO getVideoPostById(Long videoPostId) {
        VideoPost videoPost = videoPostRepository.findById(videoPostId).orElse(null);
        if (videoPost != null) {
            return videoPostMapper.videoPostToDTO(videoPost); // Use the mapper to convert to DTO
        }
        return null;
    }

    public VideoPostDTO createVideoPost(VideoPostDTO videoPostDTO) {
        VideoPost videoPost = videoPostMapper.dtoToVideoPost(videoPostDTO); // Use the mapper to convert to an entity
        VideoPost savedVideoPost = videoPostRepository.save(videoPost);
        return videoPostMapper.videoPostToDTO(savedVideoPost); // Use the mapper to convert back to DTO
    }

    public VideoPostDTO updateVideoPost(Long videoPostId, VideoPostDTO updatedVideoPostDTO) {
        if (videoPostRepository.existsById(videoPostId)) {
            VideoPost videoPost = videoPostMapper.dtoToVideoPost(updatedVideoPostDTO); // Use the mapper to convert to an entity
            videoPost.setPostId(videoPostId);
            VideoPost savedVideoPost = videoPostRepository.save(videoPost);
            return videoPostMapper.videoPostToDTO(savedVideoPost); // Use the mapper to convert back to DTO
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
