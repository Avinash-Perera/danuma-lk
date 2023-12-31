package com.avinash.danumalk.comment;

import com.avinash.danumalk.comment.*;
import com.avinash.danumalk.post.Post;
import com.avinash.danumalk.post.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements ICommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    /**
     * Creates a comment on a post.
     *
     * @param  postId      the ID of the post to comment on
     * @param  commentDTO  the comment DTO containing the details of the comment
     * @return             the created comment DTO
     */
    @Override
    @Transactional
    public CommentDTO createCommentOnPost(Long postId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Comment comment = commentMapper.dtoToComment(commentDTO);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.commentToDTO(savedComment);
    }

    /**
     * Create a reply to a comment.
     *
     * @param  parentCommentId    the ID of the parent comment
     * @param  replyCommentDTO    the DTO object containing the reply comment details
     * @return                    the DTO object representing the created reply comment
     */
    @Override
    @Transactional
    public CommentDTO createReplyToComment(Long parentCommentId, CommentDTO replyCommentDTO) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));

        // Check if the parent comment has an associated post
        if (parentComment.getPost() == null) {
            throw new IllegalArgumentException("You cannot reply to this comment.");
        }

        Comment replyComment = commentMapper.dtoToComment(replyCommentDTO);
        replyComment.setParentComment(parentComment);

        Comment savedReplyComment = commentRepository.save(replyComment);
        return commentMapper.commentToDTO(savedReplyComment);
    }

    /**
     * Updates a comment with the given commentId using the information provided in the updatedCommentDTO.
     *
     * @param  commentId              the ID of the comment to be updated
     * @param  updatedCommentDTO      the updated comment data
     * @return                        the updated comment as a CommentDTO object
     */
    @Override
    @Transactional
    public CommentDTO updateComment(Long commentId, CommentDTO updatedCommentDTO) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        Comment updatedComment = commentMapper.dtoToComment(updatedCommentDTO);
        existingComment.setContent(updatedComment.getContent());

        Comment savedComment = commentRepository.save(existingComment);
        return commentMapper.commentToDTO(savedComment);
    }

    /**
     * Deletes a comment by the comment ID.
     *
     * @param  commentId  the ID of the comment to be deleted
     */
    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        // Check if it's a top-level comment or a reply
        if (comment.getParentComment() == null) {
            // It's a top-level comment, so remove it from the post
            comment.getPost().getComments().remove(comment);
            postRepository.save(comment.getPost());
        } else {
            // It's a reply, so remove it from the parent comment
            comment.getParentComment().getReplies().remove(comment);
            commentRepository.save(comment.getParentComment());
        }

        commentRepository.delete(comment);
    }

    /**
     * Retrieves a comment by its ID.
     *
     * @param  commentId  the ID of the comment to retrieve
     * @return            the DTO representation of the retrieved comment
     */
    @Override
    @Transactional
    public CommentDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        return commentMapper.commentToDTO(comment);
    }

    /**
     * Retrieves all comments for a given post.
     *
     * @param  postId  the ID of the post
     * @return         a list of CommentDTO objects representing the comments for the post
     */
    @Override
    @Transactional
    public List<CommentDTO> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        List<Comment> comments = commentRepository.findAllByPost(post);
        return comments.stream()
                .map(commentMapper::commentToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all replies for a parent comment.
     *
     * @param  parentCommentId  the ID of the parent comment
     * @return                  a list of CommentDTO objects representing the reply comments
     */
    @Override
    @Transactional
    public List<CommentDTO> getAllRepliesForParentComment(Long parentCommentId) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));

        // Retrieve all comments where the parentComment property matches the given parent comment
        List<Comment> replyComments = commentRepository.findAllByParentComment(parentComment);

        // Convert reply comments to DTOs
        return replyComments.stream()
                .map(commentMapper::commentToDTO)
                .collect(Collectors.toList());
    }
}
