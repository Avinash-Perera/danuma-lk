package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.CommentDTO;
import com.avinash.danumalk.dto.CommentMapper;
import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.repository.CommentRepository;
import com.avinash.danumalk.repository.PostRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.commentMapper = commentMapper;
    }

    @Transactional
    public CommentDTO createCommentOnPost(Long postId, CommentDTO commentDTO) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        Comment comment = commentMapper.dtoToComment(commentDTO);
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);
        return commentMapper.commentToDTO(savedComment);
    }

    @Transactional
    public CommentDTO createReplyToComment(Long parentCommentId, CommentDTO replyCommentDTO) {
        Comment parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));

        Comment replyComment = commentMapper.dtoToComment(replyCommentDTO);
        replyComment.setParentComment(parentComment);

        Comment savedReplyComment = commentRepository.save(replyComment);
        return commentMapper.commentToDTO(savedReplyComment);
    }

    @Transactional
    public CommentDTO updateComment(Long commentId, CommentDTO updatedCommentDTO) {
        Comment existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        Comment updatedComment = commentMapper.dtoToComment(updatedCommentDTO);
        existingComment.setContent(updatedComment.getContent());

        Comment savedComment = commentRepository.save(existingComment);
        return commentMapper.commentToDTO(savedComment);
    }

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

    @Transactional
    public CommentDTO getCommentById(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        return commentMapper.commentToDTO(comment);
    }

    @Transactional
    public List<CommentDTO> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));

        List<Comment> comments = commentRepository.findAllByPost(post);
        return comments.stream()
                .map(commentMapper::commentToDTO)
                .collect(Collectors.toList());
    }
}