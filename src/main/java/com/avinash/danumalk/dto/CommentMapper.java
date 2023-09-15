package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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

        // Map reply comments to CommentDTO objects
        List<CommentDTO> replyCommentDTOs = new ArrayList<>();
        if (comment.getReplies() != null) {
            for (Comment replyComment : comment.getReplies()) {
                CommentDTO replyCommentDTO = commentToDTO(replyComment); // Recursive call
                replyCommentDTOs.add(replyCommentDTO);
            }
        }

        commentDTO.setReplyComments(replyCommentDTOs); // Use a field like 'replyComments' in CommentDTO to store the full reply comments
        commentDTO.setContent(comment.getContent());
        commentDTO.setCreatedAt(comment.getCreatedAt());
        commentDTO.setUpdatedAt(comment.getUpdatedAt());

        return commentDTO;
    }



    public Comment dtoToComment(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setCommentId(commentDTO.getCommentId());

        // Set parentComment if provided in the DTO
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

        // Set replyComments if provided in the DTO
        if (commentDTO.getReplyComments() != null && !commentDTO.getReplyComments().isEmpty()) {
            List<Comment> replyComments = new ArrayList<>();
            for (CommentDTO replyCommentDTO : commentDTO.getReplyComments()) {
                Comment replyComment = dtoToComment(replyCommentDTO); // Recursive call
                replyComments.add(replyComment);
            }
            comment.setReplies(replyComments);
        }

        comment.setContent(commentDTO.getContent());
        comment.setCreatedAt(commentDTO.getCreatedAt());
        comment.setUpdatedAt(commentDTO.getUpdatedAt());

        return comment;
    }

}