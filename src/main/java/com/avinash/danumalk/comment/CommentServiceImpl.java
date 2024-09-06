package com.avinash.danumalk.comment;

import com.avinash.danumalk.common.PageResponse;
import com.avinash.danumalk.common.ResultResponse;
import com.avinash.danumalk.exceptions.UnauthorizedAccessException;
import com.avinash.danumalk.posts.BasePostEntity;
import com.avinash.danumalk.posts.BasePostEntityRepository;
import com.avinash.danumalk.user.User;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements InterfaceCommentService{
    private final BasePostEntityRepository basePostEntityRepository;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;


    @Override
    @Transactional
    public ResultResponse<CommentResponse> createCommentOnPost(UUID postId, CommentRequest commentRequest, Authentication connectedUser) {
        /*Optimize more with criteria builder or indexing later after the mvp */
        BasePostEntity existingPost = basePostEntityRepository.findById(postId).orElseThrow(() -> new EntityNotFoundException("Post not found!"));

        User user = ((User) connectedUser.getPrincipal());
        Comment comment = commentMapper.mapToComment(commentRequest);
        comment.setOwner(user);
        comment.setPost(existingPost);
        Comment savedComment = commentRepository.save(comment);
        CommentResponse response = commentMapper.mapToCommentResponse(savedComment, connectedUser);

        return ResultResponse.<CommentResponse>builder()
                .status("OK")
                .data(response)
                .build();

    }

    @Override
    @Transactional
    public ResultResponse<CommentResponse>  createReplyToComment(UUID parentCommentId, CommentRequest replyCommentRequest, Authentication connectedUser) {
        var parentComment = commentRepository.findById(parentCommentId)
                .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));

        User user = ((User) connectedUser.getPrincipal());

        if (parentComment.getPost() == null) {
            throw new IllegalArgumentException("You cannot reply to this comment.");
        }

        Comment replyComment = commentMapper.mapToComment(replyCommentRequest);
        replyComment.setOwner(user);
        replyComment.setParentComment(parentComment);
        replyComment.setPost(parentComment.getPost());
        Comment savedComment = commentRepository.save(replyComment);
        CommentResponse response = commentMapper.mapToCommentResponse(savedComment, connectedUser);


        return ResultResponse.<CommentResponse>builder()
                .status("OK")
                .data(response)
                .build();

    }

    @Override
    public ResultResponse<CommentResponse> updateComment(UUID commentId, CommentRequest updatedCommentRequest, Authentication connectedUser) {
        User user = ((User) connectedUser.getPrincipal());
        var existingComment = commentRepository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));

        if (!existingComment.getOwner().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("Unauthorized to update this comment.");
        }

        Comment updatedComment = commentMapper.mapToComment(updatedCommentRequest);
        updatedComment.setId(existingComment.getId());
        updatedComment.setOwner(user);
        updatedComment.setPost(existingComment.getPost());
        updatedComment.setParentComment(existingComment.getParentComment());

        Comment savedComment = commentRepository.save(updatedComment);
        CommentResponse response = commentMapper.mapToCommentResponse(savedComment, connectedUser);

        return ResultResponse.<CommentResponse>builder()
                .status("OK")
                .data(response)
                .build();

    }

    @Override
    @Transactional
    public boolean deleteComment(UUID commentId, Authentication connectedUser) {
        try {
            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
            User user = ((User) connectedUser.getPrincipal());
            if (comment != null && comment.getOwner().getId().equals(user.getId())) {
                // Check if it's a top-level comment or a reply
                if (comment.getParentComment() == null) {
                    // It's a top-level comment, no additional action needed
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

    @Override
    public ResultResponse<CommentResponse> getCommentById(UUID commentId, Authentication connectedUser) {
        Comment existingComment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalStateException("Comment not found!"));
        CommentResponse response = commentMapper.mapToCommentResponse(existingComment, connectedUser);

        return ResultResponse.<CommentResponse>builder()
                .status("OK")
                .data(response)
                .build();
    }

    @Override
    public PageResponse<CommentResponse> getAllCommentsForPost(UUID postId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Comment> comments = commentRepository.findAllByPostId(postId,pageable);
        // Use a lambda expression to pass the connectedUser to the mapper
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> commentMapper.mapToCommentResponse(comment, connectedUser))
                .toList();
        return new PageResponse<>(
                commentResponses,
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.isFirst(),
                comments.isLast()
        );
    }

    @Override
    public PageResponse<CommentResponse> getAllRepliesForParentComment(UUID parentCommentId, int page, int size, Authentication connectedUser) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Comment> comments = commentRepository.findAllByPostId(parentCommentId,pageable);
        List<CommentResponse> commentResponses = comments.stream()
                .map(comment -> commentMapper.mapToCommentResponse(comment, connectedUser))
                .toList();

        return new PageResponse<>(
                commentResponses,
                comments.getNumber(),
                comments.getSize(),
                comments.getTotalElements(),
                comments.getTotalPages(),
                comments.isFirst(),
                comments.isLast()
        );

    }
}
