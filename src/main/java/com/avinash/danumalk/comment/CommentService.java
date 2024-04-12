package com.avinash.danumalk.comment;

import com.avinash.danumalk.exceptions.UnauthorizedAccessException;
import com.avinash.danumalk.post.Post;
import com.avinash.danumalk.post.PostRepository;
import com.avinash.danumalk.util.SecurityUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentService implements CommentServiceInterface {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final SecurityUtils securityUtils;


    @Override
    @Transactional
    public CommentDTO createCommentOnPost(Long postId, CommentDTO commentDTO) {
        var postOptional = postRepository.findById(postId);
        if (postOptional.isEmpty()) {
            throw new EntityNotFoundException("Post not found bro");
        }
        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();

        commentDTO.setUserId(authenticatedUserId);
        var comment = commentMapper.dtoToComment(commentDTO);
        comment.getPost().setPostId(postId);
        comment.getUser().setId(authenticatedUserId);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.commentToDTO(savedComment);
    }


    @Override
    @Transactional
    public CommentDTO createReplyToComment(Long parentCommentId, CommentDTO replyCommentDTO) {
        var parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));

        System.out.println(parentComment.getPost().getPostId());

        // Check if the parent comment has an associated post
        if (parentComment.getPost() == null) {
            throw new IllegalArgumentException("You cannot reply to this comment.");
        }

        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();

        replyCommentDTO.setUserId(authenticatedUserId);
        var replyComment = commentMapper.dtoToComment(replyCommentDTO);
        replyComment.getPost().setPostId(parentComment.getPost().getPostId());
        replyComment.setParentComment(parentComment);
        var savedReplyComment = commentRepository.save(replyComment);
        return commentMapper.commentToDTO(savedReplyComment);
    }


    @Override
    @Transactional
    public CommentDTO updateComment(Long commentId, CommentDTO updatedCommentDTO) {
        var existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();

        // Check if the authenticated user created the post
        if (!existingComment.getUser().getId().equals(authenticatedUserId)) {
            throw new UnauthorizedAccessException("Unauthorized to update this comment.");
        }
        updatedCommentDTO.setUserId(authenticatedUserId);
        var updatedComment = commentMapper.dtoToComment(updatedCommentDTO);
        updatedComment.setParentComment(existingComment.getParentComment());
        updatedComment.setCommentId(commentId);
        updatedComment.getUser().setId(authenticatedUserId);
        var savedComment = commentRepository.save(updatedComment);
        return commentMapper.commentToDTO(savedComment);
    }

    @Override
    @Transactional
    public boolean deleteComment(Long commentId) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
            Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();
            if (comment != null && comment.getUser().getId().equals(authenticatedUserId)) {
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
                return true;
            } else {
                // User is not authorized to delete the comment
                return false;
            }
        } catch (Exception ex) {
            // An exception occurred during deletion
            return false;
        }
    }



//    @Override
//    @Transactional
//    public boolean deleteComment(Long commentId) {
//        Comment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
//        Integer authenticatedUserId = securityUtils.getAuthenticatedUserId();
//        if (comment != null && comment.getUser().getId().equals(authenticatedUserId)) {
//            // Check if it's a top-level comment or a reply
//            if (comment.getParentComment() == null) {
//                // It's a top-level comment, so remove it from the post
//                comment.getPost().getComments().remove(comment);
//                postRepository.save(comment.getPost());
//            } else {
//                // It's a reply, so remove it from the parent comment
//                comment.getParentComment().getReplies().remove(comment);
//                commentRepository.save(comment.getParentComment());
//            }
//
//            commentRepository.delete(comment);
//            return true; // Deletion successful
//
//        }else {
//            throw new UnauthorizedAccessException("Unauthorized to delete this image post or post not found.");
//
//        }
//
//    }


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
