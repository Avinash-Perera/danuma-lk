package com.avinash.danumalk.service.comment;

import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.repository.CommentRepository;
import com.avinash.danumalk.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public Comment createCommentOnPost(Long postId, Comment comment) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found"));
        comment.setPost(post);
        return commentRepository.save(comment);
    }

    public Comment createReplyToComment(Long parentCommentId, Comment reply) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));
        reply.setParentComment(parentComment);
        return commentRepository.save(reply);
    }
    // Update a comment by its ID
    public Comment updateComment(Long commentId, Comment updatedComment) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        // Update the content of the existing comment
        existingComment.setContent(updatedComment.getContent());
        existingComment.setUpdatedAt(new java.sql.Date(new Date().getTime())); // Set the updated timestamp

        return commentRepository.save(existingComment);
    }

    // Delete a comment by its ID
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

    // Get a comment by its ID
    public Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
    }

    public List<Comment> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found"));
        return commentRepository.findAllByPost(post);
    }

}
