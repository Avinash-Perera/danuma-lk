package com.avinash.danumalk.reaction;

import com.avinash.danumalk.post.Post;
import com.avinash.danumalk.post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReactionControllerTest {

    @Mock
    private ReactionServiceImpl reactionService;

    @Mock
    private PostService postService;

    private ReactionController reactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reactionController = new ReactionController(reactionService, postService);
    }

    @Test
    void createLikeReaction_ValidPostId_ReturnsOkResponse() {
        // Arrange
        Long postId = 1L;
        Post post = new Post();
        post.setPostId(postId);
        Like like = new Like();
        like.setPost(post);

        when(postService.findById(postId)).thenReturn(Optional.of(post));
        when(reactionService.addReaction(like)).thenReturn(like);

        // Act
        ResponseEntity<ReactionDTO> response = reactionController.createLikeReaction(postId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ReactionDTO reactionDTO = response.getBody();
        assertEquals(postId, reactionDTO.getPostId());
        verify(postService, times(1)).findById(postId);
        verify(reactionService, times(1)).addReaction(like);
    }

    @Test
    void createLikeReaction_InvalidPostId_ReturnsNotFoundResponse() {
        // Arrange
        Long postId = 1L;
        when(postService.findById(postId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<ReactionDTO> response = reactionController.createLikeReaction(postId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(postService, times(1)).findById(postId);
        verify(reactionService, never()).addReaction(any());
    }

    // Similar tests can be written for createDislikeReaction method.

    @Test
    void removeReaction_ValidReactionId_RemovesReaction() {
        // Arrange
        Long reactionId = 1L;

        // Act
        reactionController.removeReaction(reactionId);

        // Assert
        verify(reactionService, times(1)).removeReaction(reactionId);
    }

    @Test
    void getTotalLikesForPost_ValidPostId_ReturnsTotalLikes() {
        // Arrange
        Long postId = 1L;
        int totalLikes = 5;

        when(reactionService.getTotalLikesForPost(postId)).thenReturn(totalLikes);

        // Act
        ResponseEntity<?> response = reactionController.getTotalLikesForPost(postId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(totalLikes, response.getBody());
        verify(reactionService, times(1)).getTotalLikesForPost(postId);
    }

    @Test
    void getTotalLikesForPost_InvalidPostId_ReturnsNotFoundResponse() {
        // Arrange
        Long postId = 1L;

        when(reactionService.getTotalLikesForPost(postId)).thenReturn(-1);

        // Act
        ResponseEntity<?> response = reactionController.getTotalLikesForPost(postId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(reactionService, times(1)).getTotalLikesForPost(postId);
    }

    // Similar tests can be written for getTotalDislikesForPost method.
}
