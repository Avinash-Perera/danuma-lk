package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.TextPostDTO;
import com.avinash.danumalk.dto.TextPostMapper;
import com.avinash.danumalk.model.TextPost;
import com.avinash.danumalk.repository.TextPostRepository;
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

public class TextPostServiceTest {
    @InjectMocks
    private  TextPostService textPostService;

    @Mock
    private TextPostRepository textPostRepository;

    private TextPostMapper textPostMapper = Mockito.mock(TextPostMapper.class);

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test the getImagePostById method of the TextPostService class.
     *
     * @param  @textPostId    the ID of the text post
     * @return               the TextPostDTO object
     */
    @Test
    public void testGetImagePostById_ReturnsImagePostDTO() {
        // Arrange
        Long textPostId = 1L;
        TextPost textPost = new TextPost();
        textPost.setPostId(textPostId);
        TextPostDTO expectedDTO = new TextPostDTO();
        expectedDTO.setPostId(textPostId);
        when(textPostRepository.findById(textPostId)).thenReturn(Optional.of(textPost));
        when(textPostMapper.textPostToDTO(textPost)).thenReturn(expectedDTO);

        // Act
        TextPostDTO result = textPostService.getTextPostById(textPostId);

        // Assert
        assertEquals(expectedDTO, result);
    }

    /**
     * Test case for the `getImagePostById` method when it returns null.
     *
     * @param  @None
     * @return None
     */
    @Test
    public void testGetImagePostById_ReturnsNull() {
        // Arrange
        Long textPostId = 1L;
        when(textPostRepository.findById(textPostId)).thenReturn(Optional.empty());

        // Act
        TextPostDTO result = textPostService.getTextPostById(textPostId);

        // Assert
        assertNull(result);
    }


    /**
     * Test for creating an image post with a valid ImagePostDTO.
     *
     * @param  @inputImagePostDTO  the ImagePostDTO to create the post from
     * @return                    the created ImagePostDTO
     */
    @Test
    public void testCreateImagePost_ValidImagePostDTO_ReturnsCreatedImagePostDTO() {
        // Arrange
        TextPostDTO inputImagePostDTO = new TextPostDTO();
        inputImagePostDTO.setTitle("Test Image Post");
        TextPost inputImagePostEntity = new TextPost();
        inputImagePostEntity.setTitle("Test Image Post");

        TextPost savedImagePostEntity = new TextPost();
        savedImagePostEntity.setPostId(1L);
        savedImagePostEntity.setTitle("Test Image Post");

        TextPostDTO expectedImagePostDTO = new TextPostDTO();
        expectedImagePostDTO.setPostId(1L);
        expectedImagePostDTO.setTitle("Test Image Post");

        when(textPostMapper.dtoToTextPost(inputImagePostDTO)).thenReturn(inputImagePostEntity);
        when(textPostRepository.save(inputImagePostEntity)).thenReturn(savedImagePostEntity);
        when(textPostMapper.textPostToDTO(savedImagePostEntity)).thenReturn(expectedImagePostDTO);

        // Act
        TextPostDTO result = textPostService.createTextPost(inputImagePostDTO);

        // Assert
        assertNotNull(result);
        assertEquals(expectedImagePostDTO.getPostId(), result.getPostId());
        assertEquals(expectedImagePostDTO.getTitle(), result.getTitle());
    }

    /**
     * Test case to verify that when a null ImagePostDTO is passed to the createTextPost() method,
     * null is returned.
     *
     * @param  @inputImagePostDTO  the input ImagePostDTO object
     * @return                    the TextPostDTO object returned by the createTextPost() method
     */
    @Test
    public void testCreateImagePost_NullImagePostDTO_ReturnsNull() {
        // Arrange
        TextPostDTO inputImagePostDTO = null;

        // Act
        TextPostDTO result = textPostService.createTextPost(inputImagePostDTO);

        // Assert
        assertNull(result);
    }

    /**
     * Test for updating an existing image post by its ID and returning the updated image post DTO.
     *
     * @param  @textPostId             the ID of the existing image post
     * @param  @updatedImagePostDTO    the updated image post DTO
     * @return                        the updated image post DTO
     */
    @Test
    public void testUpdateImagePost_ExistingImagePostId_ReturnsUpdatedImagePostDTO() {
        // Arrange
        Long textPostId = 1L;
        TextPostDTO updatedImagePostDTO = new TextPostDTO();
        TextPost textPost = new TextPost();
        TextPost savedImagePost = new TextPost();
        when(textPostRepository.existsById(textPostId)).thenReturn(true);
        when(textPostMapper.dtoToTextPost(updatedImagePostDTO)).thenReturn(textPost);
        when(textPostRepository.save(textPost)).thenReturn(savedImagePost);
        when(textPostMapper.textPostToDTO(savedImagePost)).thenReturn(updatedImagePostDTO);

        // Act
        TextPostDTO result = textPostService.updateTextPost(textPostId, updatedImagePostDTO);

        // Assert
        Assertions.assertEquals(updatedImagePostDTO, result);
        verify(textPostRepository, times(1)).existsById(textPostId);
        verify(textPostMapper, times(1)).dtoToTextPost(updatedImagePostDTO);
        verify(textPostRepository, times(1)).save(textPost);
        verify(textPostMapper, times(1)).textPostToDTO(savedImagePost);
    }

    /**
     * Test case to verify that the updateImagePost method returns null when the image post with the given ID does not exist.
     *
     * @param  @paramName	description of parameter
     * @return         	description of return value
     */
    @Test
    public void testUpdateImagePost_NonExistingImagePostId_ReturnsNull() {
        // Arrange
        Long textPostId = 1L;
        TextPostDTO updatedImagePostDTO = new TextPostDTO();
        when(textPostRepository.existsById(textPostId)).thenReturn(false);

        // Act
        TextPostDTO result = textPostService.updateTextPost(textPostId, updatedImagePostDTO);

        // Assert
        Assertions.assertNull(result);
        verify(textPostRepository, times(1)).existsById(textPostId);
        verify(textPostMapper, never()).dtoToTextPost(updatedImagePostDTO);
        verify(textPostRepository, never()).save(any());
        verify(textPostMapper, never()).textPostToDTO(any());
    }


    /**
     * Test for deleting an existing image post.
     *
     * @param  @existingId  the ID of the existing image post
     * @return             true if the image post is successfully deleted, false otherwise
     */
    @Test
    public void testDeleteImagePost_existingId_returnsTrue() {
        // Arrange
        Long textPostId = 123L;
        when(textPostRepository.existsById(textPostId)).thenReturn(true);

        // Act
        boolean result = textPostService.deleteTextPost(textPostId);

        // Assert
        assertTrue(result);
        verify(textPostRepository).deleteById(textPostId);
    }

    /**
     * A test case to verify the behavior of the deleteTextPost method when given a non-existing text post ID.
     *
     * @param  @textPostId  the ID of the text post to delete
     * @return             true if the text post was successfully deleted, false otherwise
     */
    @Test
    public void testDeleteImagePost_nonExistingId_returnsFalse() {
        // Arrange
        Long textPostId = 123L;
        when(textPostRepository.existsById(textPostId)).thenReturn(false);

        // Act
        boolean result = textPostService.deleteTextPost(textPostId);

        // Assert
        assertFalse(result);
    }
}
