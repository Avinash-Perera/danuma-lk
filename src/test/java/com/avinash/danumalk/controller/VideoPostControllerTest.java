package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.VideoPostDTO;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.service.VideoPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class VideoPostControllerTest {
    @InjectMocks
    private VideoPostController videoPostController;

    @Mock
    private VideoPostService videoPostService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for creating a valid VideoPost.
     */
    @Test
    void createVideoPost_validPostType_returnsCreatedPost() {
        // Arrange
        VideoPostDTO videoPostDTO = new VideoPostDTO();
        videoPostDTO.setPostType(PostType.VIDEO);
        when(videoPostService.createVideoPost(videoPostDTO)).thenReturn(videoPostDTO);

        // Act
        VideoPostDTO result = videoPostController.createVideoPost(videoPostDTO);

        // Assert
        assertNotNull(result);
        assertEquals(videoPostDTO, result);
    }

    /**
     * Test case for attempting to create a VideoPost with an invalid post type.
     */
    @Test
    void createVideoPost_invalidPostType_throwsIllegalArgumentException() {
        // Arrange
        VideoPostDTO videoPostDTO = new VideoPostDTO();
        videoPostDTO.setPostType(PostType.TEXT);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> videoPostController.createVideoPost(videoPostDTO));
    }

    /**
     * Test case for updating an existing VideoPost with valid data.
     */
    @Test
    public void testUpdateVideoPost_existingVideoPostFound_returnUpdatedVideoPostDTO() {
        // Arrange
        Long videoPostId = 1L;
        VideoPostDTO updatedVideoPostDTO = new VideoPostDTO();
        updatedVideoPostDTO.setPostType(PostType.VIDEO);
        updatedVideoPostDTO.setTitle("Updated Title");
        updatedVideoPostDTO.setVideoUrl("https://example.com/video");
        updatedVideoPostDTO.setVideoDescription("Updated Description");

        VideoPostDTO existingVideoPostDTO = new VideoPostDTO();
        existingVideoPostDTO.setPostType(PostType.VIDEO);
        existingVideoPostDTO.setTitle("Original Title");
        existingVideoPostDTO.setVideoUrl("https://example.com/original-video");
        existingVideoPostDTO.setVideoDescription("Original Description");

        when(videoPostService.getVideoPostById(videoPostId)).thenReturn(existingVideoPostDTO);
        when(videoPostService.updateVideoPost(videoPostId, existingVideoPostDTO)).thenReturn(updatedVideoPostDTO);

        // Act
        ResponseEntity<VideoPostDTO> response = videoPostController.updateVideoPost(videoPostId, updatedVideoPostDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedVideoPostDTO, response.getBody());
    }

    /**
     * Test case for attempting to update an existing VideoPost with an invalid post type.
     */
    @Test
    public void testUpdateVideoPost_existingVideoPostFound_invalidPostType_returnBadRequest() {
        // Arrange
        Long videoPostId = 1L;
        VideoPostDTO updatedVideoPostDTO = new VideoPostDTO();
        updatedVideoPostDTO.setPostType(PostType.TEXT);

        VideoPostDTO existingVideoPostDTO = new VideoPostDTO();
        existingVideoPostDTO.setPostType(PostType.VIDEO);

        when(videoPostService.getVideoPostById(videoPostId)).thenReturn(existingVideoPostDTO);

        // Act
        ResponseEntity<VideoPostDTO> response = videoPostController.updateVideoPost(videoPostId, updatedVideoPostDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Test case for attempting to change the post type when updating an existing VideoPost.
     */
    @Test
    public void testUpdateVideoPost_changePostType_returnBadRequest() {
        // Arrange
        Long videoPostId = 1L;
        VideoPostDTO updatedVideoPostDTO = new VideoPostDTO();
        updatedVideoPostDTO.setPostType(PostType.IMAGE);

        VideoPostDTO existingVideoPostDTO = new VideoPostDTO();
        existingVideoPostDTO.setPostType(PostType.VIDEO);

        when(videoPostService.getVideoPostById(videoPostId)).thenReturn(existingVideoPostDTO);

        // Act
        ResponseEntity<VideoPostDTO> response = videoPostController.updateVideoPost(videoPostId, updatedVideoPostDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    /**
     * Test case for attempting to update a non-existing VideoPost.
     */
    @Test
    public void testUpdateVideoPost_existingVideoPostNotFound_returnNotFound() {
        // Arrange
        Long videoPostId = 1L;
        VideoPostDTO updatedVideoPostDTO = new VideoPostDTO();
        updatedVideoPostDTO.setPostType(PostType.VIDEO);

        when(videoPostService.getVideoPostById(videoPostId)).thenReturn(null);

        // Act
        ResponseEntity<VideoPostDTO> response = videoPostController.updateVideoPost(videoPostId, updatedVideoPostDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /**
     * Test case for successfully deleting an existing VideoPost.
     */
    @Test
    public void testDeleteVideoPost_Success() {
        // Arrange
        Long videoPostId = 1L;
        VideoPostDTO existingVideoPostDTO = new VideoPostDTO();
        existingVideoPostDTO.setPostType(PostType.VIDEO);

        when(videoPostService.getVideoPostById(videoPostId)).thenReturn(existingVideoPostDTO);
        when(videoPostService.deleteVideoPost(videoPostId)).thenReturn(true);

        // Act
        boolean result = videoPostController.deleteVideoPost(videoPostId);

        // Assert
        assertTrue(result);
        verify(videoPostService, times(1)).getVideoPostById(videoPostId);
        verify(videoPostService, times(1)).deleteVideoPost(videoPostId);
    }

    /**
     * Test case for attempting to delete a non-existing VideoPost.
     */
    @Test
    public void testDeleteVideoPost_VideoPostNotFound() {
        // Arrange
        Long videoPostId = 1L;

        when(videoPostService.getVideoPostById(videoPostId)).thenReturn(null);

        // Act
        boolean result = videoPostController.deleteVideoPost(videoPostId);

        // Assert
        assertFalse(result);
        verify(videoPostService, times(1)).getVideoPostById(videoPostId);
        verify(videoPostService, never()).deleteVideoPost(videoPostId);
    }

    /**
     * Test case for attempting to delete a VideoPost with an invalid post type.
     */
    @Test
    public void testDeleteVideoPost_InvalidPostType() {
        // Arrange
        Long videoPostId = 1L;
        VideoPostDTO existingVideoPostDTO = new VideoPostDTO();
        existingVideoPostDTO.setPostType(PostType.IMAGE);

        when(videoPostService.getVideoPostById(videoPostId)).thenReturn(existingVideoPostDTO);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> videoPostController.deleteVideoPost(videoPostId));
        verify(videoPostService, times(1)).getVideoPostById(videoPostId);
        verify(videoPostService, never()).deleteVideoPost(videoPostId);
    }
}
