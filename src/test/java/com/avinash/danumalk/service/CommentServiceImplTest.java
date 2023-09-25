package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.CommentDTO;
import com.avinash.danumalk.dto.CommentMapper;
import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.repository.CommentRepository;
import com.avinash.danumalk.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.*;



public class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private CommentServiceImpl commentService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Test case for creating a comment on a post.
     *
     * @param  @postId      the ID of the post to comment on
     * @param  @commentDTO  the comment DTO containing the comment details
     * @return             the resulting comment DTO after creation
     */
    @Test
    public void testCreateCommentOnPost() {
        // Arrange
        Long postId = 1L;
        CommentDTO commentDTO = new CommentDTO();
        Post post = new Post();
        Comment comment = new Comment();
        Comment savedComment = new Comment();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(commentMapper.dtoToComment(commentDTO)).thenReturn(comment);
        when(commentRepository.save(comment)).thenReturn(savedComment);

        // Act
        CommentDTO result = commentService.createCommentOnPost(postId, commentDTO);

        // Assert
        verify(postRepository, times(1)).findById(postId);
        verify(commentMapper, times(1)).dtoToComment(commentDTO);
        verify(commentRepository, times(1)).save(comment);
        assert result == commentMapper.commentToDTO(savedComment);
    }

    /**
     * Test case for the `createReplyToComment` function.
     *
     * @param  @parentCommentId  the ID of the parent comment
     * @param  @replyCommentDTO  the DTO object for the reply comment
     * @return                  the DTO object for the created reply comment
     */
    @Test
    public void testCreateReplyToComment() {
        // Arrange
        Long parentCommentId = 1L;
        CommentDTO replyCommentDTO = new CommentDTO();
        Comment parentComment = new Comment();
        Comment replyComment = new Comment();
        Comment savedReplyComment = new Comment();

        ReflectionTestUtils.setField(parentComment, "post", new Post());

        when(commentRepository.findById(parentCommentId)).thenReturn(Optional.of(parentComment));
        when(commentMapper.dtoToComment(replyCommentDTO)).thenReturn(replyComment);
        when(commentRepository.save(replyComment)).thenReturn(savedReplyComment);

        // Act
        CommentDTO result = commentService.createReplyToComment(parentCommentId, replyCommentDTO);

        // Assert
        verify(commentRepository, times(1)).findById(parentCommentId);
        verify(commentMapper, times(1)).dtoToComment(replyCommentDTO);
        verify(commentRepository, times(1)).save(replyComment);
        assert result == commentMapper.commentToDTO(savedReplyComment);
    }

    /**
     * Test method for creating a reply to a comment when the parent comment is invalid.
     *
     * @param  @parentCommentId   the ID of the parent comment
     * @param  @replyCommentDTO   the DTO containing the reply comment information
     * @return                   description of return value
     */
    @Test
    public void testCreateReplyToComment_InvalidParentComment() {
        // Arrange
        Long parentCommentId = 1L;
        CommentDTO replyCommentDTO = new CommentDTO();

        when(commentRepository.findById(parentCommentId)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            commentService.createReplyToComment(parentCommentId, replyCommentDTO);
            assert false; // Should throw EntityNotFoundException
        } catch (EntityNotFoundException e) {
            // Expected exception
        }
    }

    /**
     * Test case for deleting a top-level comment.
     *
     * @param  @commentId  the ID of the comment to be deleted
     * @return            void
     */
    @Test
    public void testDeleteComment_TopLevelComment() {
        // Arrange
        Long commentId = 1L;
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        Post post = new Post();
        post.getComments().add(comment);
        comment.setPost(post);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        commentService.deleteComment(commentId);

        // Assert
        verify(commentRepository).delete(comment);
        verify(postRepository).save(post);
    }

    /**
     * Test case for the deleteComment method when deleting a reply comment.
     *
     * @param  @paramName    description of parameter
     * @return              description of return value
     */
    @Test
    public void testDeleteComment_ReplyComment() {
        // Arrange
        Long commentId = 2L;
        Comment comment = new Comment();
        comment.setCommentId(commentId);
        Comment parentComment = new Comment();
        parentComment.setReplies(new ArrayList<>()); // Create a new non-null List object
        parentComment.getReplies().add(comment);
        comment.setParentComment(parentComment);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // Act
        commentService.deleteComment(commentId);

        // Assert
        verify(commentRepository).delete(comment);
        verify(commentRepository).save(parentComment);
    }

    /**
     * Test for deleting an invalid comment.
     *
     * @param  @commentId  the ID of the comment to be deleted
     * @return            nothing
     */
    @Test
    public void testDeleteComment_InvalidComment() {
        // Arrange
        Long commentId = 1L;

        when(commentRepository.findById(commentId)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            commentService.deleteComment(commentId);
            assert false; // Should throw EntityNotFoundException
        } catch (EntityNotFoundException e) {
            // Expected exception
        }
    }

    /**
     * Test for getting a comment by its ID.
     *
     * @param  @commentId  the ID of the comment to retrieve
     * @return            the DTO representation of the comment
     */
    @Test
    public void testGetCommentById() {
        // Arrange
        Long commentId = 1L;
        Comment comment = new Comment();
        CommentDTO commentDTO = new CommentDTO();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));
        when(commentMapper.commentToDTO(comment)).thenReturn(commentDTO);

        // Act
        CommentDTO result = commentService.getCommentById(commentId);

        // Assert
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentMapper, times(1)).commentToDTO(comment);
        assert result == commentDTO;
    }
}
