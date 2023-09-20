package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.VideoPost;
import org.springframework.stereotype.Component;

@Component
public class VideoPostMapper {

    /**
     * Converts a VideoPost object to a VideoPostDTO object.
     *
     * @param  videoPost  the VideoPost object to be converted
     * @return            the converted VideoPostDTO object
     */
    public VideoPostDTO videoPostToDTO(VideoPost videoPost) {
        VideoPostDTO videoPostDTO = mapToDTO(videoPost);
        videoPostDTO.setVideoUrl(videoPost.getVideoUrl());
        videoPostDTO.setVideoDescription(videoPost.getVideoDescription());
        return videoPostDTO;
    }


    /**
     * Converts a VideoPostDTO object to a VideoPost object.
     *
     * @param  videoPostDTO  the VideoPostDTO object to be converted
     * @return               the converted VideoPost object
     */
    public VideoPost dtoToVideoPost(VideoPostDTO videoPostDTO) {
        VideoPost videoPost = mapToVideoPost(videoPostDTO);
        videoPost.setVideoUrl(videoPostDTO.getVideoUrl());
        videoPost.setVideoDescription(videoPostDTO.getVideoDescription());
        return videoPost;
    }

    //helper methods
    private VideoPostDTO mapToDTO(VideoPost videoPost) {
        VideoPostDTO videoPostDTO = new VideoPostDTO();
        videoPostDTO.setPostId(videoPost.getPostId());
        videoPostDTO.setTitle(videoPost.getTitle());
        videoPostDTO.setPostType(videoPost.getPostType());
        videoPostDTO.setCreatedAt(videoPost.getCreatedAt());
        videoPostDTO.setUpdatedAt(videoPost.getUpdatedAt());
        return videoPostDTO;
    }

    private VideoPost mapToVideoPost(VideoPostDTO videoPostDTO) {
        VideoPost videoPost = new VideoPost();
        videoPost.setPostId(videoPostDTO.getPostId());
        videoPost.setTitle(videoPostDTO.getTitle());
        videoPost.setPostType(videoPostDTO.getPostType());
        videoPost.setCreatedAt(videoPostDTO.getCreatedAt());
        videoPost.setUpdatedAt(videoPostDTO.getUpdatedAt());
        return videoPost;
    }
}
