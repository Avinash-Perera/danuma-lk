package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.PostDTO;
import com.avinash.danumalk.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class PostControllerTest {
    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    /**
     * Test case for the getAllPosts function.
     */
    @Test
    void testGetAllPosts() {
        // Arrange
        PostDTO post1 = new PostDTO();
        post1.setPostId(1L);
        post1.setTitle("Post 1");

        PostDTO post2 = new PostDTO();
        post2.setPostId(2L);
        post2.setTitle("Post 2");

        when(postService.getAllPosts()).thenReturn(Arrays.asList(post1, post2));

        // Act
        List<PostDTO> result = postController.getAllPosts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Post 1", result.get(0).getTitle());
        assertEquals("Post 2", result.get(1).getTitle());
    }

    /**
     * Test case for the `getPostById` method when a post with the given ID exists.
     *
     * @param  @postId  the ID of the post to retrieve
     * @return         the response entity containing the retrieved post
     */
    @Test
    void testGetPostById_existingPost() {
        // Arrange
        Long postId = 1L;
        PostDTO post = new PostDTO();
        post.setPostId(postId);
        post.setTitle("Test Post");

        when(postService.getPostById(postId)).thenReturn(post);

        // Act
        ResponseEntity<PostDTO> responseEntity = postController.getPostById(postId);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(postId, responseEntity.getBody().getPostId());
        assertEquals("Test Post", responseEntity.getBody().getTitle());
    }

    /**
     * Test case for the getPostById method when a non-existing post is requested.
     *
     * @param  @postId  the ID of the post
     * @return         the response entity containing the post DTO
     */
    @Test
    void testGetPostById_nonExistingPost() {
        // Arrange
        Long postId = 1L;
        when(postService.getPostById(postId)).thenReturn(null);

        // Act
        ResponseEntity<PostDTO> responseEntity = postController.getPostById(postId);

        // Assert
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
}
