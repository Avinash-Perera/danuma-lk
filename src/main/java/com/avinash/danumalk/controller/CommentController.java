package com.avinash.danumalk.controller;

import com.avinash.danumalk.dto.CommentDTO;
import com.avinash.danumalk.service.CommentServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin
@AllArgsConstructor
public class CommentController {
    private final CommentServiceImpl commentServiceImpl;

    /**
     * Creates a comment on a post.
     *
     * @param  postId       the ID of the post
     * @param  commentDTO   the comment DTO containing the comment details
     * @return              the created comment DTO
     */
    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDTO> createCommentOnPost(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentServiceImpl.createCommentOnPost(postId, commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    /**
     * Creates a reply to a comment.
     *
     * @param  parentCommentId  the ID of the parent comment
     * @param  replyCommentDTO  the DTO containing the reply comment data
     * @return                  the newly created reply comment DTO
     */
    @PostMapping("/reply/{parentCommentId}")
    public ResponseEntity<CommentDTO> createReplyToComment(@PathVariable Long parentCommentId, @RequestBody CommentDTO replyCommentDTO) {
        CommentDTO createdReply = commentServiceImpl.createReplyToComment(parentCommentId, replyCommentDTO);
        return new ResponseEntity<>(createdReply, HttpStatus.CREATED);
    }

    /**
     * Updates a comment with the given commentId using the provided updatedCommentDTO.
     *
     * @param  commentId             the ID of the comment to be updated
     * @param  updatedCommentDTO     the updated comment data
     * @return                       the updated comment as a CommentDTO object
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO updatedCommentDTO) {
        CommentDTO updated = commentServiceImpl.updateComment(commentId, updatedCommentDTO);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Deletes a comment by comment ID.
     *
     * @param  commentId  the ID of the comment to be deleted
     * @return            a ResponseEntity with NO_CONTENT status
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentServiceImpl.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Retrieves a comment by its ID.
     *
     * @param  commentId  the ID of the comment to retrieve
     * @return            the comment DTO if found, or a not found status if not
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
        CommentDTO commentDTO = commentServiceImpl.getCommentById(commentId);
        if (commentDTO != null) {
            return new ResponseEntity<>(commentDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    /**
     * Retrieves all comments for a specific post.
     *
     * @param  postId  the ID of the post
     * @return         a list of CommentDTO objects representing the comments for the post
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@PathVariable Long postId) {
        List<CommentDTO> commentDTOs = commentServiceImpl.getAllCommentsForPost(postId);
        return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
    }


    /**
     * Retrieves all the replies for a parent comment.
     *
     * @param  parentCommentId  the ID of the parent comment
     * @return                  a list of comment DTOs representing the replies
     */
    @GetMapping("/replies/{parentCommentId}")
    public ResponseEntity<List<CommentDTO>> getAllRepliesForParentComment(@PathVariable Long parentCommentId) {
        List<CommentDTO> replyComments = commentServiceImpl.getAllRepliesForParentComment(parentCommentId);
        return new ResponseEntity<>(replyComments, HttpStatus.OK);
    }

}
