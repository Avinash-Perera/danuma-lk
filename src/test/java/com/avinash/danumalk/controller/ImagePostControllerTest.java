package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.ImagePostDTO;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.service.ImagePostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ImagePostControllerTest {
    @InjectMocks
    private ImagePostController imagePostController;

    @Mock
    private ImagePostService imagePostService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void createImagePost_validPostType_returnsCreatedPost() {
        // Arrange
        ImagePostDTO imagePostDTO = new ImagePostDTO();
        imagePostDTO.setPostType(PostType.IMAGE);
        when(imagePostService.createImagePost(imagePostDTO)).thenReturn(imagePostDTO);

        // Act
        ImagePostDTO result = imagePostController.createImagePost(imagePostDTO);

        // Assert
        assertNotNull(result);
        assertEquals(imagePostDTO, result);
    }

    @Test
    void createImagePost_invalidPostType_throwsIllegalArgumentException() {
        // Arrange
        ImagePostDTO imagePostDTO = new ImagePostDTO();
        imagePostDTO.setPostType(PostType.TEXT);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> imagePostController.createImagePost(imagePostDTO));
    }

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
        ResponseEntity<ImagePostDTO> imageDTO = imagePostController.updateImagePost(imagePostId, updatedImagePostDTO); // Change variable name to 'imageDTO'

        // Assert
        assertEquals(HttpStatus.OK, imageDTO.getStatusCode()); // Change 'responseEntity' to 'imageDTO'
        assertNotNull(imageDTO.getBody()); // Change 'responseEntity' to 'imageDTO'
        assertEquals(updatedImagePostDTO, imageDTO.getBody()); // Change 'responseEntity' to 'imageDTO'
        verify(imagePostService, times(1)).getImagePostById(imagePostId);
        verify(imagePostService, times(1)).updateImagePost(imagePostId, updatedImagePostDTO);
    }


    @Test
    public void testUpdateImagePost_invalidPostType() {
        // Prepare
        Long imagePostId = 1L;
        ImagePostDTO updatedImagePostDTO = new ImagePostDTO();
        updatedImagePostDTO.setPostType(PostType.VIDEO);

        ImagePostDTO existingImagePostDTO = new ImagePostDTO();
        existingImagePostDTO.setPostType(PostType.VIDEO);

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(existingImagePostDTO);

        // Execute and Verify
        assertThrows(IllegalArgumentException.class, () -> imagePostController.updateImagePost(imagePostId, updatedImagePostDTO));
    }

    @Test
    public void testUpdateImagePost_changePostType() {
        // Prepare
        Long imagePostId = 1L;
        ImagePostDTO updatedImagePostDTO = new ImagePostDTO();
        updatedImagePostDTO.setPostType(PostType.IMAGE);

        ImagePostDTO existingImagePostDTO = new ImagePostDTO();
        existingImagePostDTO.setPostType(PostType.VIDEO);

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(existingImagePostDTO);

        // Execute and Verify
        assertThrows(IllegalArgumentException.class, () -> imagePostController.updateImagePost(imagePostId, updatedImagePostDTO));
    }

    @Test
    public void testUpdateImagePost_ImagePostNotFound() {
        // Arrange
        Long imagePostId = 1L;
        ImagePostDTO updatedImagePostDTO = new ImagePostDTO();
        updatedImagePostDTO.setPostType(PostType.IMAGE);
        // Set other properties of updatedImagePostDTO as needed

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(null);

        // Act
        ResponseEntity<ImagePostDTO> responseEntity = imagePostController.updateImagePost(imagePostId, updatedImagePostDTO);

        // Assert
        assertEquals(404, responseEntity.getStatusCodeValue()); // Check if HTTP status code is Not Found
        verify(imagePostService, times(1)).getImagePostById(imagePostId);
        verify(imagePostService, never()).updateImagePost(anyLong(), any());
    }

    @Test
    public void testDeleteImagePost_Success() {
        // Arrange
        Long imagePostId = 1L;
        ImagePostDTO existingImagePostDTO = new ImagePostDTO();
        existingImagePostDTO.setPostType(PostType.IMAGE);

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(existingImagePostDTO);
        when(imagePostService.deleteImagePost(imagePostId)).thenReturn(true);

        // Act
        boolean result = imagePostController.deleteImagePost(imagePostId);

        // Assert
        assertTrue(result);
        verify(imagePostService, times(1)).getImagePostById(imagePostId);
        verify(imagePostService, times(1)).deleteImagePost(imagePostId);
    }

    @Test
    public void testDeleteImagePost_ImagePostNotFound() {
        // Arrange
        Long imagePostId = 1L;

        when(imagePostService.getImagePostById(imagePostId)).thenReturn(null);

        // Act
        boolean result = imagePostController.deleteImagePost(imagePostId);

        // Assert
        assertFalse(result);
        verify(imagePostService, times(1)).getImagePostById(imagePostId);
        verify(imagePostService, never()).deleteImagePost(imagePostId);
    }

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
