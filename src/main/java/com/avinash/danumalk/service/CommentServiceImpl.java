package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.CommentDTO;
import com.avinash.danumalk.dto.CommentMapper;
import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.repository.CommentRepository;
import com.avinash.danumalk.repository.PostRepository;
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

    @Override
    @Transactional
    public CommentDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        return commentMapper.commentToDTO(comment);
    }

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
