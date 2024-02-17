package com.avinash.danumalk.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the ImagePostController class.
 */
public class ImagePostControllerTest {
    @InjectMocks
    private ImagePostController imagePostController;

    @Mock
    private ImagePostService imagePostService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for creating an image post with a valid post type.
     */
    @Test
    void createImagePost_validPostType_returnsCreatedPost() {
        // Arrange
        ImagePostDTO imagePostDTO = new ImagePostDTO();
        imagePostDTO.setPostType(PostType.IMAGE);
        when(imagePostService.createImagePost(imagePostDTO)).thenReturn(imagePostDTO);

        // Act
        ImagePostDTO result = (ImagePostDTO) imagePostController.createImagePost(imagePostDTO).getBody();

        // Assert
        assertNotNull(result);
        assertEquals(imagePostDTO, result);
    }

    /**
     * Test case for attempting to create an image post with an invalid post type, which should throw an exception.
     */
    @Test
    void createImagePost_invalidPostType_throwsIllegalArgumentException() {
        // Arrange
        ImagePostDTO imagePostDTO = new ImagePostDTO();
        imagePostDTO.setPostType(PostType.TEXT);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> imagePostController.createImagePost(imagePostDTO));
    }

    /**
     * Test case for updating an existing image post with valid data.
     */
    @Test
    public void testUpdateImagePost() {
        // Arrange
        Long imagePostId = 1L;
        ImagePostDTO existingImagePostDTO = new ImagePostDTO();
        existingImagePostDTO.setPostType(PostType.IMAGE);

        ImagePostDTO updatedImagePostDTO = new ImagePostDTO();
        updatedImagePostDTO.setPostType(PostType.IMAGE);
        updatedImagePostDTO.setTitle("Updated Title");
        updatedImagePostDTO.setImageUrl("Updated URL");
        updatedImagePostDTO.setImageDescription("Updated Description");

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(existingImagePostDTO);
        when(imagePostService.updateImagePost(imagePostId, updatedImagePostDTO)).thenReturn(updatedImagePostDTO);

        // Act
        ResponseEntity<ImagePostDTO> imageDTO = (ResponseEntity<ImagePostDTO>) imagePostController.updateImagePost(imagePostId, updatedImagePostDTO); // Change variable name to 'imageDTO'

        // Assert
        assertEquals(HttpStatus.OK, imageDTO.getStatusCode()); // Change 'responseEntity' to 'imageDTO'
        assertNotNull(imageDTO.getBody()); // Change 'responseEntity' to 'imageDTO'
        assertEquals(updatedImagePostDTO, imageDTO.getBody()); // Change 'responseEntity' to 'imageDTO'
        verify(imagePostService, times(1)).getImagePostById(imagePostId);
        verify(imagePostService, times(1)).updateImagePost(imagePostId, updatedImagePostDTO);
    }

    /**
     * Test case for attempting to update an image post with an invalid post type, which should throw an exception.
     */
    @Test
    public void testUpdateImagePost_invalidPostType() {
        // Prepare
        Long imagePostId = 1L;
        ImagePostDTO updatedImagePostDTO = new ImagePostDTO();
        updatedImagePostDTO.setPostType(PostType.TEXT);

        ImagePostDTO existingImagePostDTO = new ImagePostDTO();
        existingImagePostDTO.setPostType(PostType.TEXT);

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(existingImagePostDTO);

        // Execute and Verify
        assertThrows(IllegalArgumentException.class, () -> imagePostController.updateImagePost(imagePostId, updatedImagePostDTO));
    }

    /**
     * Test case for attempting to change the post type when updating an image post, which should throw an exception.
     */
    @Test
    public void testUpdateImagePost_changePostType() {
        // Prepare
        Long imagePostId = 1L;
        ImagePostDTO updatedImagePostDTO = new ImagePostDTO();
        updatedImagePostDTO.setPostType(PostType.IMAGE);

        ImagePostDTO existingImagePostDTO = new ImagePostDTO();
        existingImagePostDTO.setPostType(PostType.TEXT);

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(existingImagePostDTO);

        // Execute and Verify
        assertThrows(IllegalArgumentException.class, () -> imagePostController.updateImagePost(imagePostId, updatedImagePostDTO));
    }

    /**
     * Test case for attempting to update a non-existent image post, which should return a 404 status code.
     */
    @Test
    public void testUpdateImagePost_ImagePostNotFound() {
        // Arrange
        Long imagePostId = 1L;
        ImagePostDTO updatedImagePostDTO = new ImagePostDTO();
        updatedImagePostDTO.setPostType(PostType.IMAGE);
        // Set other properties of updatedImagePostDTO as needed

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(null);

        // Act
        ResponseEntity<ImagePostDTO> responseEntity = (ResponseEntity<ImagePostDTO>) imagePostController.updateImagePost(imagePostId, updatedImagePostDTO);

        // Assert
        assertEquals(404, responseEntity.getStatusCodeValue()); // Check if HTTP status code is Not Found
        verify(imagePostService, times(1)).getImagePostById(imagePostId);
        verify(imagePostService, never()).updateImagePost(anyLong(), any());
    }

    /**
     * Test case for successfully deleting an image post.
     */
    @Test
    public void testDeleteImagePost_Success() {
        // Arrange
        Long imagePostId = 1L;
        ImagePostDTO existingImagePostDTO = new ImagePostDTO();
        existingImagePostDTO.setPostType(PostType.IMAGE);

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(existingImagePostDTO);
        when(imagePostService.deleteImagePost(imagePostId)).thenReturn(true);

        // Act
        boolean result = imagePostController.deleteImagePost(imagePostId).hasBody();

        // Assert
        assertTrue(result);
        verify(imagePostService, times(1)).getImagePostById(imagePostId);
        verify(imagePostService, times(1)).deleteImagePost(imagePostId);
    }

    /**
     * Test case for attempting to delete a non-existent image post, which should return false.
     */
    @Test
    public void testDeleteImagePost_ImagePostNotFound() {
        // Arrange
        Long imagePostId = 1L;

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(null);

        // Act
        boolean result = imagePostController.deleteImagePost(imagePostId).hasBody();

        // Assert
        assertFalse(result);
        verify(imagePostService, times(1)).getImagePostById(imagePostId);
        verify(imagePostService, never()).deleteImagePost(imagePostId);
    }

    /**
     * Test case for attempting to delete an image post with an invalid post type, which should throw an exception.
     */
    @Test
    public void testDeleteImagePost_InvalidPostType() {
        // Arrange
        Long imagePostId = 1L;
        ImagePostDTO existingImagePostDTO = new ImagePostDTO();
        existingImagePostDTO.setPostType(PostType.TEXT); // Invalid post type

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(existingImagePostDTO);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> imagePostController.deleteImagePost(imagePostId));
        verify(imagePostService, times(1)).getImagePostById(imagePostId);
        verify(imagePostService, never()).deleteImagePost(imagePostId);
    }
}
