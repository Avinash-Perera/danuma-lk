package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.Post;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommentMapper {

    public CommentDTO commentToDTO(Comment comment) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setCommentId(comment.getCommentId());

        // Set postId if provided in the Comment entity
        if (comment.getPost() != null) {
            commentDTO.setPostId(comment.getPost().getPostId());
        }

        commentDTO.setParentCommentId(comment.getParentComment() != null ? comment.getParentComment().getCommentId() : null);

        // Check for null and initialize an empty list if replies is null
        List<Long> replyCommentIds = comment.getReplies() != null ?
                comment.getReplies().stream()
                        .map(Comment::getCommentId)
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        commentDTO.setReplyCommentIds(replyCommentIds);
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUpdatedAt(comment.getUpdatedAt());

        return commentDTO;
    }



    public Comment dtoToComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setCommentId(commentDTO.getCommentId());

        // Set parentCommentId if provided in the DTO
        if (commentDTO.getParentCommentId() != null) {
            Comment parentComment = new Comment();
            parentComment.setCommentId(commentDTO.getParentCommentId());
            comment.setParentComment(parentComment);
        }

        // Set postId if provided in the DTO
        if (commentDTO.getPostId() != null) {
            Post post = new Post();
            post.setPostId(commentDTO.getPostId());
            comment.setPost(post);
        }

        // Set replyCommentIds if provided in the DTO
        if (commentDTO.getReplyCommentIds() != null && !commentDTO.getReplyCommentIds().isEmpty()) {
            List<Comment> replyComments = commentDTO.getReplyCommentIds().stream()
                    .map(replyCommentId -> {
                        Comment replyComment = new Comment();
                        replyComment.setCommentId(replyCommentId);
                        return replyComment;
                    })
                    .collect(Collectors.toList());
            comment.setReplies(replyComments);
        }

        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setUpdatedAt(commentDTO.getUpdatedAt());



        return comment;

    }

}