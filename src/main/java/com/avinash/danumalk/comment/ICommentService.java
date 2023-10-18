package com.avinash.danumalk.comment;

import com.avinash.danumalk.comment.CommentDTO;

import java.util.List;

public interface ICommentService {
    CommentDTO createCommentOnPost(Long postId, CommentDTO commentDTO);
    CommentDTO createReplyToComment(Long parentCommentId, CommentDTO replyCommentDTO);
    CommentDTO updateComment(Long commentId, CommentDTO updatedCommentDTO);
    void deleteComment(Long commentId);
    CommentDTO getCommentById(Long commentId);
    List<CommentDTO> getAllCommentsForPost(Long postId);
    List<CommentDTO> getAllRepliesForParentComment(Long parentCommentId);
}
