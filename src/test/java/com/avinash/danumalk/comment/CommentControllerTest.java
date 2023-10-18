package com.avinash.danumalk.comment;

import com.avinash.danumalk.comment.CommentController;
import com.avinash.danumalk.comment.CommentDTO;
import com.avinash.danumalk.comment.CommentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class CommentControllerTest {
    @Mock
    private CommentServiceImpl commentService;

    @InjectMocks
    private CommentController commentController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test case for creating a comment on a post.
     */
    @Test
    public void testCreateCommentOnPost() {
        // Arrange
        Long postId = 1L;
        CommentDTO commentDTO = new CommentDTO();
        CommentDTO createdComment = new CommentDTO();

        when(commentService.createCommentOnPost(postId, commentDTO)).thenReturn(createdComment);

        // Act
        ResponseEntity<CommentDTO> response = commentController.createCommentOnPost(postId, commentDTO);

        // Assert
        verify(commentService, times(1)).createCommentOnPost(postId, commentDTO);
        assert response.getStatusCode() == HttpStatus.CREATED;
        assert response.getBody() == createdComment;
    }

    /**
     * Test the updateComment() method in the CommentController class.
     */
    @Test
    public void testUpdateComment() {
        // Arrange
        Long commentId = 1L;
        CommentDTO updatedCommentDTO = new CommentDTO();
        CommentDTO updatedComment = new CommentDTO();

        when(commentService.updateComment(commentId, updatedCommentDTO)).thenReturn(updatedComment);

        // Act
        ResponseEntity<CommentDTO> response = commentController.updateComment(commentId, updatedCommentDTO);

        // Assert
        verify(commentService, times(1)).updateComment(commentId, updatedCommentDTO);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == updatedComment;
    }

    /**
     * Test case for the deleteComment method in the CommentController class.
     */

    @Test
    public void testDeleteComment() {
        // Arrange
        Long commentId = 1L;

        // Act
        ResponseEntity<Void> response = commentController.deleteComment(commentId);

        // Assert
        verify(commentService, times(1)).deleteComment(commentId);
        assert response.getStatusCode() == HttpStatus.NO_CONTENT;
    }

    /**
     * Test for the getCommentById() method.
     */
    @Test
    public void testGetCommentById() {
        // Arrange
        Long commentId = 1L;
        CommentDTO commentDTO = new CommentDTO();

        when(commentService.getCommentById(commentId)).thenReturn(commentDTO);

        // Act
        ResponseEntity<CommentDTO> response = commentController.getCommentById(commentId);

        // Assert
        verify(commentService, times(1)).getCommentById(commentId);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == commentDTO;
    }

    /**
     * Test case for the getAllCommentsForPost method.
     */
    @Test
    public void testGetAllCommentsForPost() {
        // Arrange
        Long postId = 1L;
        List<CommentDTO> commentDTOs = new ArrayList<>();

        when(commentService.getAllCommentsForPost(postId)).thenReturn(commentDTOs);

        // Act
        ResponseEntity<List<CommentDTO>> response = commentController.getAllCommentsForPost(postId);

        // Assert
        verify(commentService, times(1)).getAllCommentsForPost(postId);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == commentDTOs;
    }

    /**
     * Test for the getAllRepliesForParentComment function.
     */
    @Test
    public void testGetAllRepliesForParentComment() {
        // Arrange
        Long parentCommentId = 1L;
        List<CommentDTO> replyComments = new ArrayList<>();

        when(commentService.getAllRepliesForParentComment(parentCommentId)).thenReturn(replyComments);

        // Act
        ResponseEntity<List<CommentDTO>> response = commentController.getAllRepliesForParentComment(parentCommentId);

        // Assert
        verify(commentService, times(1)).getAllRepliesForParentComment(parentCommentId);
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody() == replyComments;
    }
}
