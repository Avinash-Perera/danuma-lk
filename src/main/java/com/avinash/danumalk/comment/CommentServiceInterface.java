package com.avinash.danumalk.comment;

import java.util.List;

public interface CommentServiceInterface {
    CommentDTO createCommentOnPost(Long postId, CommentDTO commentDTO);
    CommentDTO createReplyToComment(Long parentCommentId, CommentDTO replyCommentDTO);
    CommentDTO updateComment(Long commentId, CommentDTO updatedCommentDTO);
    boolean deleteComment(Long commentId);
    CommentDTO getCommentById(Long commentId);
    List<CommentDTO> getAllCommentsForPost(Long postId);
    List<CommentDTO> getAllRepliesForParentComment(Long parentCommentId);
}
