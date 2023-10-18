package com.avinash.danumalk.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class PostServiceTest {
    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    private PostMapper postMapper = Mockito.mock(PostMapper.class);

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test case for the getAllPosts method.
     *
     * This method tests the functionality of the getAllPosts method in the PostService class.
     *
     * @return         	The list of PostDTO objects representing all the posts.
     */
    @Test
    public void testGetAllPosts() {
        // Arrange
        PostRepository postRepository = mock(PostRepository.class);
        PostMapper postMapper = mock(PostMapper.class);
        PostService postService = new PostService(postRepository, postMapper);

        List<Post> mockPosts = new ArrayList<>();
        mockPosts.add(new Post());
        mockPosts.add(new Post());

        List<PostDTO> expected = new ArrayList<>();
        expected.add(new PostDTO());
        expected.add(new PostDTO());

        when(postRepository.findAllByOrderByCreatedAtDesc()).thenReturn(mockPosts);
        when(postMapper.postToDTO(any(Post.class))).thenReturn(new PostDTO());

        // Act
        List<PostDTO> actual = postService.getAllPosts();

        // Assert
        assertEquals(expected, actual);
        verify(postRepository, times(1)).findAllByOrderByCreatedAtDesc();
        verify(postMapper, times(mockPosts.size())).postToDTO(any(Post.class));
    }

    /**
     * Test case: testGetPostById_ExistingPost_ReturnsPostDTO
     *
     * Description: This test case verifies that the getPostById method
     * returns the expected PostDTO when a post with the given ID exists
     * in the post repository.
     *
     * @param  @postId  the ID of the post to retrieve
     * @return         the PostDTO object representing the retrieved post
     */

    @Test
    public void testGetPostById_ExistingPost_ReturnsPostDTO() {
        // Arrange
        Long postId = 1L;
        Post post = new Post();
        PostDTO expectedDto = new PostDTO();
        Mockito.when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        Mockito.when(postMapper.postToDTO(post)).thenReturn(expectedDto);

        // Act
        PostDTO result = postService.getPostById(postId);

        // Assert
        assertEquals(expectedDto, result);
    }

    /**
     * Test for finding an existing post by ID and returning an Optional with the post.
     *
     * @param  @postId  the ID of the post to find
     * @return         an Optional containing the expected post
     */
    @Test
    public void testFindById_ExistingPost_ReturnsOptionalWithPost() {
        // Arrange
        Long postId = 1L;
        Post expectedPost = new Post();
        expectedPost.setPostId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(expectedPost));

        // Act
        Optional<Post> result = postRepository.findById(postId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(expectedPost, result.get());
    }


    /**
     * Test case for findById_InvalidId_ReturnsEmptyOptional.
     *
     * @param  @postId  the ID of the post to find
     * @return         an empty Optional if the post is not found
     */
    @Test
    void findById_InvalidId_ReturnsEmptyOptional() {
        // Arrange
        Long postId = 100L;

        // Act
        Optional<Post> result = postService.findById(postId);

        // Assert
        assertTrue(result.isEmpty());
    }

}
