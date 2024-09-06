package com.avinash.danumalk.comment;

import com.avinash.danumalk.common.PageResponse;
import com.avinash.danumalk.common.ResultResponse;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.UUID;

public interface InterfaceCommentService {
    ResultResponse<CommentResponse> createCommentOnPost(UUID postId, CommentRequest commentRequest, Authentication connectedUser);

    ResultResponse<CommentResponse>  createReplyToComment(UUID parentCommentId, CommentRequest commentRequest, Authentication connectedUser);

    ResultResponse<CommentResponse> updateComment(UUID commentId, CommentRequest updatedCommentRequest, Authentication connectedUser);

    boolean deleteComment(UUID commentId,Authentication connectedUser);

    ResultResponse<CommentResponse> getCommentById(UUID commentId, Authentication connectedUser);

    PageResponse<CommentResponse> getAllCommentsForPost(UUID postId, int page, int size, Authentication connectedUser);

    PageResponse<CommentResponse> getAllRepliesForParentComment(UUID parentCommentId, int page, int size, Authentication connectedUser);
//
//    PageResponse<CommentResponse> getAllCommentsForPost(UUID postId, int page, int size, Authentication connectedUser) ;
//
//    PageResponse<CommentResponse> getAllRepliesForParentComment(UUID parentCommentId, int page, int size, Authentication connectedUser);
}
