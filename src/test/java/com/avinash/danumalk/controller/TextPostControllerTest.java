package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.TextPostDTO;
import com.avinash.danumalk.model.PostType;
import com.avinash.danumalk.service.TextPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the TextPostController class.
 */
public class TextPostControllerTest {
    @InjectMocks
    private TextPostController textPostController;

    @Mock
    private TextPostService textPostService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for creating a valid text post.
     */
    @Test
    void createTextPost_validPostType_returnsCreatedPost() {
        // Arrange
        TextPostDTO textPostDTO = new TextPostDTO();
        textPostDTO.setPostType(PostType.TEXT);
        when(textPostService.createTextPost(textPostDTO)).thenReturn(textPostDTO);

        // Act
        TextPostDTO result = textPostController.createTextPost(textPostDTO);

        // Assert
        assertNotNull(result);
        assertEquals(textPostDTO, result);
    }

    /**
     * Test case for creating a text post with an invalid post type.
     */
    @Test
    void createTextPost_invalidPostType_throwsIllegalArgumentException() {
        // Arrange
        TextPostDTO textPostDTO = new TextPostDTO();
        textPostDTO.setPostType(PostType.IMAGE);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> textPostController.createTextPost(textPostDTO));
    }

    /**
     * Test case for updating an existing text post.
     */
    @Test
    public void testUpdateTextPost() {
        // Arrange
        Long textPostId = 1L;
        TextPostDTO existingTextPostDTO = new TextPostDTO();
        existingTextPostDTO.setPostType(PostType.TEXT);

        TextPostDTO updatedTextPostDTO = new TextPostDTO();
        updatedTextPostDTO.setPostType(PostType.TEXT);
        updatedTextPostDTO.setTitle("Updated Title");
        updatedTextPostDTO.setContent("Updated content");

        when(textPostService.getTextPostById(textPostId)).thenReturn(existingTextPostDTO);
        when(textPostService.updateTextPost(textPostId, updatedTextPostDTO)).thenReturn(updatedTextPostDTO);

        // Act
        ResponseEntity<TextPostDTO> textPostDTO = textPostController.updateTextPost(textPostId, updatedTextPostDTO);

        // Assert
        assertEquals(HttpStatus.OK, textPostDTO.getStatusCode());
        assertNotNull(textPostDTO.getBody());
        assertEquals(updatedTextPostDTO, textPostDTO.getBody());
        verify(textPostService, times(1)).getTextPostById(textPostId);
        verify(textPostService, times(1)).updateTextPost(textPostId, updatedTextPostDTO);
    }

    /**
     * Test case for updating a text post with an invalid post type.
     */
    @Test
    public void testUpdateImagePost_invalidPostType() {
        // Prepare
        Long textPostId = 1L;
        TextPostDTO updatedTextPostDTO = new TextPostDTO();
        updatedTextPostDTO.setPostType(PostType.IMAGE);

        TextPostDTO existingTextPostDTO = new TextPostDTO();
        existingTextPostDTO.setPostType(PostType.IMAGE);

        when(textPostService.getTextPostById(textPostId)).thenReturn(existingTextPostDTO);

        // Execute and Verify
        assertThrows(IllegalArgumentException.class, () -> textPostController.updateTextPost(textPostId, updatedTextPostDTO));
    }

    /**
     * Test case for updating a text post by changing its post type.
     */
    @Test
    public void testUpdateImagePost_changePostType() {
        // Prepare
        Long textPostId = 1L;
        TextPostDTO updatedTextPostDTO = new TextPostDTO();
        updatedTextPostDTO.setPostType(PostType.TEXT);

        TextPostDTO existingTextPostDTO = new TextPostDTO();
        existingTextPostDTO.setPostType(PostType.IMAGE);

        when(textPostService.getTextPostById(textPostId)).thenReturn(existingTextPostDTO);

        // Execute and Verify
        assertThrows(IllegalArgumentException.class, () -> textPostController.updateTextPost(textPostId, updatedTextPostDTO));
    }

    /**
     * Test case for updating a non-existing text post.
     */
    @Test
    public void testUpdateImagePost_ImagePostNotFound() {
        // Arrange
        Long textPostId = 1L;
        TextPostDTO updatedTextPostDTO = new TextPostDTO();
        updatedTextPostDTO.setPostType(PostType.IMAGE);
        // Set other properties of updatedTextPostDTO as needed

        when(textPostService.getTextPostById(textPostId)).thenReturn(null);

        // Act
        ResponseEntity<TextPostDTO> responseEntity = textPostController.updateTextPost(textPostId, updatedTextPostDTO);

        // Assert
        assertEquals(404, responseEntity.getStatusCodeValue());
        verify(textPostService, times(1)).getTextPostById(textPostId);
        verify(textPostService, never()).updateTextPost(anyLong(), any());
    }

    /**
     * Test case for deleting an existing text post successfully.
     */
    @Test
    public void testDeleteImagePost_Success() {
        // Arrange
        Long textPostId = 1L;
        TextPostDTO existingTextPostDTO = new TextPostDTO();
        existingTextPostDTO.setPostType(PostType.TEXT);

        when(textPostService.getTextPostById(textPostId)).thenReturn(existingTextPostDTO);
        when(textPostService.deleteTextPost(textPostId)).thenReturn(true);

        // Act
        boolean result = textPostController.deleteTextPost(textPostId);

        // Assert
        assertTrue(result);
        verify(textPostService, times(1)).getTextPostById(textPostId);
        verify(textPostService, times(1)).deleteTextPost(textPostId);
    }

    /**
     * Test case for attempting to delete a non-existing text post.
     */
    @Test
    public void testDeleteImagePost_ImagePostNotFound() {
        // Arrange
        Long textPostId = 1L;

        when(textPostService.getTextPostById(textPostId)).thenReturn(null);

        // Act
        boolean result = textPostController.deleteTextPost(textPostId);

        // Assert
        assertFalse(result);
        verify(textPostService, times(1)).getTextPostById(textPostId);
        verify(textPostService, never()).deleteTextPost(textPostId);
    }

    /**
     * Test case for attempting to delete a text post with an invalid post type.
     */
    @Test
    public void testDeleteImagePost_InvalidPostType() {
        // Arrange
        Long textPostId = 1L;
        TextPostDTO existingTextPostDTO = new TextPostDTO();
        existingTextPostDTO.setPostType(PostType.IMAGE); // Invalid post type

        when(textPostService.getTextPostById(textPostId)).thenReturn(existingTextPostDTO);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> textPostController.deleteTextPost(textPostId));
        verify(textPostService, times(1)).getTextPostById(textPostId);
        verify(textPostService, never()).deleteTextPost(textPostId);
    }
}
