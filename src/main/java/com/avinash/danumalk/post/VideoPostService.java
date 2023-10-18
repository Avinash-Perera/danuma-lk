package com.avinash.danumalk.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class VideoPostService {
    private final VideoPostRepository videoPostRepository;
    private final VideoPostMapper videoPostMapper; // Add the VideoPostMapper

    /**
     * Retrieves all video posts from the database and converts them to DTOs using a mapper.
     *
     * @return  a list of VideoPostDTO objects representing the video posts
     */
    public List<VideoPostDTO> getAllVideoPosts() {
        List<VideoPost> videoPosts = videoPostRepository.findAll();
        return Collections.singletonList(videoPostMapper.videoPostToDTO((VideoPost) videoPosts)); // Use the mapper to convert to DTOs
    }

    /**
     * Retrieves a VideoPostDTO object by its ID.
     *
     * @param  videoPostId  the ID of the video post to retrieve
     * @return              the corresponding VideoPostDTO object, or null if not found
     */
    public VideoPostDTO getVideoPostById(Long videoPostId) {
        VideoPost videoPost = videoPostRepository.findById(videoPostId).orElse(null);
        if (videoPost != null) {
            return videoPostMapper.videoPostToDTO(videoPost); // Use the mapper to convert to DTO
        }
        return null;
    }

    /**
     * Creates a new video post.
     *
     * @param  videoPostDTO  the DTO object containing the video post data
     * @return               the DTO object representing the created video post
     */
    public VideoPostDTO createVideoPost(VideoPostDTO videoPostDTO) {
        VideoPost videoPost = videoPostMapper.dtoToVideoPost(videoPostDTO); // Use the mapper to convert to an entity
        VideoPost savedVideoPost = videoPostRepository.save(videoPost);
        return videoPostMapper.videoPostToDTO(savedVideoPost); // Use the mapper to convert back to DTO
    }

    /**
     * Updates a video post with the given ID.
     *
     * @param  videoPostId            the ID of the video post to update
     * @param  updatedVideoPostDTO    the updated video post DTO
     * @return                        the updated video post DTO if the post exists, otherwise null
     */
    public VideoPostDTO updateVideoPost(Long videoPostId, VideoPostDTO updatedVideoPostDTO) {
        if (videoPostRepository.existsById(videoPostId)) {
            VideoPost videoPost = videoPostMapper.dtoToVideoPost(updatedVideoPostDTO); // Use the mapper to convert to an entity
            videoPost.setPostId(videoPostId);
            VideoPost savedVideoPost = videoPostRepository.save(videoPost);
            return videoPostMapper.videoPostToDTO(savedVideoPost); // Use the mapper to convert back to DTO
        }
        return null; // VideoPost not found
    }

    /**
     * Deletes a video post with the specified ID.
     *
     * @param  videoPostId  the ID of the video post to delete
     * @return              true if the video post was deleted successfully, false otherwise
     */
    public boolean deleteVideoPost(Long videoPostId) {
        if (videoPostRepository.existsById(videoPostId)) {
            videoPostRepository.deleteById(videoPostId);
            return true;
        }
        return false; // VideoPost not found
    }
}
