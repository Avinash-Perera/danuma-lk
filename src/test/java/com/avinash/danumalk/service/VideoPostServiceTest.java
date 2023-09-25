package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.VideoPostDTO;
import com.avinash.danumalk.dto.VideoPostMapper;
import com.avinash.danumalk.model.VideoPost;
import com.avinash.danumalk.repository.VideoPostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VideoPostServiceTest {
    @InjectMocks
    private  VideoPostService videoPostService;

    @Mock
    private VideoPostRepository videoPostRepository;

    private VideoPostMapper videoPostMapper = Mockito.mock(VideoPostMapper.class);

    
    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for the method getVideoPostById in the VideoPostService class.
     * This test verifies that the method returns the expected VideoPostDTO object
     * when given a valid videoPostId.
     *
     * @param  @videoPostId  The ID of the video post to retrieve.
     * @return              The expected VideoPostDTO object.
     */
    @Test
    public void testGetVideoPostById_ReturnsVideoPostDTO() {
        // Arrange
        Long videoPostId = 1L;
        VideoPost videoPost = new VideoPost();
        videoPost.setPostId(videoPostId);
        VideoPostDTO expectedDTO = new VideoPostDTO();
        expectedDTO.setPostId(videoPostId);
        when(videoPostRepository.findById(videoPostId)).thenReturn(Optional.of(videoPost));
        when(videoPostMapper.videoPostToDTO(videoPost)).thenReturn(expectedDTO);

        // Act
        VideoPostDTO result = videoPostService.getVideoPostById(videoPostId);

        // Assert
        assertEquals(expectedDTO, result);
    }

    /**
     * Test case to verify the behavior of the `getVideoPostById` method when
     * the specified video post ID does not exist in the repository.
     *
     * @param  @videoPostId  the ID of the video post to retrieve
     * @return              the video post DTO if found, null otherwise
     */
    @Test
    public void testGetVideoPostById_ReturnsNull() {
        // Arrange
        Long videoPostId = 1L;
        when(videoPostRepository.findById(videoPostId)).thenReturn(Optional.empty());

        // Act
        VideoPostDTO result = videoPostService.getVideoPostById(videoPostId);

        // Assert
        assertNull(result);
    }


    /**
     * Test case for the `createVideoPost` method.
     *
     * @param  @inputImagePostDTO	The VideoPostDTO object containing the input data for the test case.
     * @return              		The created VideoPostDTO object as a result of the test case.
     */
    @Test
    public void testCreateVideoPost_ValidVideoPostDTO_ReturnsCreatedVideoPostDTO() {
        // Arrange
        VideoPostDTO inputImagePostDTO = new VideoPostDTO();
        inputImagePostDTO.setTitle("Test Image Post");
        VideoPost inputImagePostEntity = new VideoPost();
        inputImagePostEntity.setTitle("Test Image Post");

        VideoPost savedImagePostEntity = new VideoPost();
        savedImagePostEntity.setPostId(1L);
        savedImagePostEntity.setTitle("Test Image Post");

        VideoPostDTO expectedImagePostDTO = new VideoPostDTO();
        expectedImagePostDTO.setPostId(1L);
        expectedImagePostDTO.setTitle("Test Image Post");

        when(videoPostMapper.dtoToVideoPost(inputImagePostDTO)).thenReturn(inputImagePostEntity);
        when(videoPostRepository.save(inputImagePostEntity)).thenReturn(savedImagePostEntity);
        when(videoPostMapper.videoPostToDTO(savedImagePostEntity)).thenReturn(expectedImagePostDTO);

        // Act
        VideoPostDTO result = videoPostService.createVideoPost(inputImagePostDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedImagePostDTO.getPostId(), result.getPostId());
        assertEquals(expectedImagePostDTO.getTitle(), result.getTitle());
    }

    /**
     * Test case to verify the behavior of the createVideoPost method when a null VideoPostDTO is provided.
     *
     * @param  @inputImagePostDTO  the VideoPostDTO object to be created
     * @return                    the VideoPostDTO object created by the createVideoPost method
     */
    @Test
    public void testCreateVideoPost_NullVideoPostDTO_ReturnsNull() {
        // Arrange
        VideoPostDTO inputImagePostDTO = null;

        // Act
        VideoPostDTO result = videoPostService.createVideoPost(inputImagePostDTO);

        // Assert
        assertNull(result);
    }

    /**
     * Test the updateVideoPost method when an existing video post ID is provided.
     *
     * @param  @videoPostId              The ID of the existing video post.
     * @param  @updatedImagePostDTO      The updated VideoPostDTO object.
     * @return                          The updated VideoPostDTO object.
     */
    @Test
    public void testUpdateVideoPost_ExistingVideoPostId_ReturnsUpdatedVideoPostDTO() {
        // Arrange
        Long videoPostId = 1L;
        VideoPostDTO updatedImagePostDTO = new VideoPostDTO();
        VideoPost videoPost = new VideoPost();
        VideoPost savedImagePost = new VideoPost();
        when(videoPostRepository.existsById(videoPostId)).thenReturn(true);
        when(videoPostMapper.dtoToVideoPost(updatedImagePostDTO)).thenReturn(videoPost);
        when(videoPostRepository.save(videoPost)).thenReturn(savedImagePost);
        when(videoPostMapper.videoPostToDTO(savedImagePost)).thenReturn(updatedImagePostDTO);

        // Act
        VideoPostDTO result = videoPostService.updateVideoPost(videoPostId, updatedImagePostDTO);

        // Assert
        Assertions.assertEquals(updatedImagePostDTO, result);
        verify(videoPostRepository, times(1)).existsById(videoPostId);
        verify(videoPostMapper, times(1)).dtoToVideoPost(updatedImagePostDTO);
        verify(videoPostRepository, times(1)).save(videoPost);
        verify(videoPostMapper, times(1)).videoPostToDTO(savedImagePost);
    }

    /**
     * Test case to verify that the method 'updateVideoPost' returns null
     * when the provided videoPostId does not exist in the videoPostRepository.
     *
     * @param  @videoPostId            the ID of the video post to be updated
     * @param  @updatedImagePostDTO    the updated VideoPostDTO object
     * @return                        null, indicating that the update was unsuccessful
     */
    @Test
    public void testUpdateVideoPost_NonExistingVideoPostId_ReturnsNull() {
        // Arrange
        Long videoPostId = 1L;
        VideoPostDTO updatedImagePostDTO = new VideoPostDTO();
        when(videoPostRepository.existsById(videoPostId)).thenReturn(false);

        // Act
        VideoPostDTO result = videoPostService.updateVideoPost(videoPostId, updatedImagePostDTO);

        // Assert
        Assertions.assertNull(result);
        verify(videoPostRepository, times(1)).existsById(videoPostId);
        verify(videoPostMapper, never()).dtoToVideoPost(updatedImagePostDTO);
        verify(videoPostRepository, never()).save(any());
        verify(videoPostMapper, never()).videoPostToDTO(any());
    }


    /**
     * Test for deleting a video post by an existing id.
     *
     * @param  @videoPostId  the id of the video post to be deleted
     * @return              true if the video post was successfully deleted, false otherwise
     */
    @Test
    public void testDeleteVideoPost_existingId_returnsTrue() {
        // Arrange
        Long videoPostId = 123L;
        when(videoPostRepository.existsById(videoPostId)).thenReturn(true);

        // Act
        boolean result = videoPostService.deleteVideoPost(videoPostId);

        // Assert
        assertTrue(result);
        verify(videoPostRepository).deleteById(videoPostId);
    }


    /**
     * A test case for the deleteVideoPost method when the video post ID does not exist.
     *
     * @param  @videoPostId  the ID of the video post
     * @return              false if the video post is not deleted, true otherwise
     */
    @Test
    public void testDeleteVideoPost_nonExistingId_returnsFalse() {
        // Arrange
        Long videoPostId = 123L;
        when(videoPostRepository.existsById(videoPostId)).thenReturn(false);

        // Act
        boolean result = videoPostService.deleteVideoPost(videoPostId);

        // Assert
        assertFalse(result);
    }
    
    
}
