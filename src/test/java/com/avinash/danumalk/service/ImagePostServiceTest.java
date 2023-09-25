package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.ImagePostDTO;
import com.avinash.danumalk.dto.ImagePostMapper;
import com.avinash.danumalk.model.ImagePost;
import com.avinash.danumalk.repository.ImagePostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ImagePostServiceTest {
    @InjectMocks
    private  ImagePostService imagePostService;

    @Mock
    private ImagePostRepository imagePostRepository;

    private ImagePostMapper imagePostMapper = Mockito.mock(ImagePostMapper.class);

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for the method getImagePostById of the ImagePostService class.
     * It verifies that the method returns the expected ImagePostDTO object
     * when given a valid imagePostId.
     *
     * @param  @imagePostId  the ID of the image post
     * @return              the expected ImagePostDTO object
     */
    @Test
    public void testGetImagePostById_ReturnsImagePostDTO() {
        // Arrange
        Long imagePostId = 1L;
        ImagePost imagePost = new ImagePost();
        imagePost.setPostId(imagePostId);
        ImagePostDTO expectedDTO = new ImagePostDTO();
        expectedDTO.setPostId(imagePostId);
        when(imagePostRepository.findById(imagePostId)).thenReturn(Optional.of(imagePost));
        when(imagePostMapper.imagePostToDTO(imagePost)).thenReturn(expectedDTO);

        // Act
        ImagePostDTO result = imagePostService.getImagePostById(imagePostId);

        // Assert
        assertEquals(expectedDTO, result);
    }

    /**
     * Test case for the getImagePostById method when it returns null.
     *
     * @param  @imagePostId  the ID of the image post
     * @return              the image post DTO object
     */
    @Test
    public void testGetImagePostById_ReturnsNull() {
        // Arrange
        Long imagePostId = 1L;
        when(imagePostRepository.findById(imagePostId)).thenReturn(Optional.empty());

        // Act
        ImagePostDTO result = imagePostService.getImagePostById(imagePostId);

        // Assert
        assertNull(result);
    }


    /**
     * Test case to verify that the createImagePost method correctly creates a new image post with the given inputImagePostDTO.
     *
     * @param  @inputImagePostDTO  The ImagePostDTO object containing the details of the image post to be created.
     * @return                    The created ImagePostDTO object with the generated postId and other details.
     */
    @Test
    public void testCreateImagePost_ValidImagePostDTO_ReturnsCreatedImagePostDTO() {
        // Arrange
        ImagePostDTO inputImagePostDTO = new ImagePostDTO();
        inputImagePostDTO.setTitle("Test Image Post");
        ImagePost inputImagePostEntity = new ImagePost();
        inputImagePostEntity.setTitle("Test Image Post");

        ImagePost savedImagePostEntity = new ImagePost();
        savedImagePostEntity.setPostId(1L);
        savedImagePostEntity.setTitle("Test Image Post");

        ImagePostDTO expectedImagePostDTO = new ImagePostDTO();
        expectedImagePostDTO.setPostId(1L);
        expectedImagePostDTO.setTitle("Test Image Post");

        when(imagePostMapper.dtoToImagePost(inputImagePostDTO)).thenReturn(inputImagePostEntity);
        when(imagePostRepository.save(inputImagePostEntity)).thenReturn(savedImagePostEntity);
        when(imagePostMapper.imagePostToDTO(savedImagePostEntity)).thenReturn(expectedImagePostDTO);

        // Act
        ImagePostDTO result = imagePostService.createImagePost(inputImagePostDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedImagePostDTO.getPostId(), result.getPostId());
        assertEquals(expectedImagePostDTO.getTitle(), result.getTitle());
    }

    /**
     * Test case to verify that the method 'createImagePost' returns null when given a null 'ImagePostDTO' parameter.
     *
     * @param  @inputImagePostDTO  the 'ImagePostDTO' object that is set to null
     * @return                    null, indicating that the method returns null
     */
    @Test
    public void testCreateImagePost_NullImagePostDTO_ReturnsNull() {
        // Arrange
        ImagePostDTO inputImagePostDTO = null;

        // Act
        ImagePostDTO result = imagePostService.createImagePost(inputImagePostDTO);

        // Assert
        assertNull(result);
    }

    /**
     * Test case for the updateImagePost method in the ImagePostService class.
     *
     * @param  @imagePostId          the ID of the existing image post
     * @param  @updatedImagePostDTO  the updated image post DTO
     * @return                      the updated image post DTO
     */
    @Test
    public void testUpdateImagePost_ExistingImagePostId_ReturnsUpdatedImagePostDTO() {
        // Arrange
        Long imagePostId = 1L;
        ImagePostDTO updatedImagePostDTO = new ImagePostDTO();
        ImagePost imagePost = new ImagePost();
        ImagePost savedImagePost = new ImagePost();
        when(imagePostRepository.existsById(imagePostId)).thenReturn(true);
        when(imagePostMapper.dtoToImagePost(updatedImagePostDTO)).thenReturn(imagePost);
        when(imagePostRepository.save(imagePost)).thenReturn(savedImagePost);
        when(imagePostMapper.imagePostToDTO(savedImagePost)).thenReturn(updatedImagePostDTO);

        // Act
        ImagePostDTO result = imagePostService.updateImagePost(imagePostId, updatedImagePostDTO);

        // Assert
        Assertions.assertEquals(updatedImagePostDTO, result);
        verify(imagePostRepository, times(1)).existsById(imagePostId);
        verify(imagePostMapper, times(1)).dtoToImagePost(updatedImagePostDTO);
        verify(imagePostRepository, times(1)).save(imagePost);
        verify(imagePostMapper, times(1)).imagePostToDTO(savedImagePost);
    }

    /**
     * Test case for the updateImagePost method in the ImagePostService class.
     *
     * @param  @imagePostId          the ID of the existing image post
     * @param  @updatedImagePostDTO  the updated image post DTO
     * @return                      the updated image post DTO
     */
    @Test
    public void testUpdateImagePost_NonExistingImagePostId_ReturnsNull() {
        // Arrange
        Long imagePostId = 1L;
        ImagePostDTO updatedImagePostDTO = new ImagePostDTO();
        when(imagePostRepository.existsById(imagePostId)).thenReturn(false);

        // Act
        ImagePostDTO result = imagePostService.updateImagePost(imagePostId, updatedImagePostDTO);

        // Assert
        Assertions.assertNull(result);
        verify(imagePostRepository, times(1)).existsById(imagePostId);
        verify(imagePostMapper, never()).dtoToImagePost(updatedImagePostDTO);
        verify(imagePostRepository, never()).save(any());
        verify(imagePostMapper, never()).imagePostToDTO(any());
    }


    /**
     * Test case to verify that deleting an existing image post returns true.
     *
     * @param  @imagePostId  the ID of the image post to be deleted
     * @return              true if the image post is deleted successfully, false otherwise
     */
    @Test
    public void testDeleteImagePost_existingId_returnsTrue() {
        // Arrange
        Long imagePostId = 123L;
        when(imagePostRepository.existsById(imagePostId)).thenReturn(true);

        // Act
        boolean result = imagePostService.deleteImagePost(imagePostId);

        // Assert
        assertTrue(result);
        verify(imagePostRepository).deleteById(imagePostId);
    }

    /**
     * Test case for the deleteImagePost method when the image post ID does not exist.
     *
     * @param  @imagePostId    the ID of the image post to be deleted
     * @return                false if the image post was not deleted, true otherwise
     */
    @Test
    public void testDeleteImagePost_nonExistingId_returnsFalse() {
        // Arrange
        Long imagePostId = 123L;
        when(imagePostRepository.existsById(imagePostId)).thenReturn(false);

        // Act
        boolean result = imagePostService.deleteImagePost(imagePostId);

        // Assert
        assertFalse(result);
    }
}
