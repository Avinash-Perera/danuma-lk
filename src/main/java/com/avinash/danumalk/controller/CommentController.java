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

    @PostMapping("/post/{postId}")
    public ResponseEntity<CommentDTO> createCommentOnPost(@PathVariable Long postId, @RequestBody CommentDTO commentDTO) {
        CommentDTO createdComment = commentServiceImpl.createCommentOnPost(postId, commentDTO);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @PostMapping("/reply/{parentCommentId}")
    public ResponseEntity<CommentDTO> createReplyToComment(@PathVariable Long parentCommentId, @RequestBody CommentDTO replyCommentDTO) {
        CommentDTO createdReply = commentServiceImpl.createReplyToComment(parentCommentId, replyCommentDTO);
        return new ResponseEntity<>(createdReply, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long commentId, @RequestBody CommentDTO updatedCommentDTO) {
        CommentDTO updated = commentServiceImpl.updateComment(commentId, updatedCommentDTO);
        if (updated != null) {
            return new ResponseEntity<>(updated, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentServiceImpl.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Long commentId) {
        CommentDTO commentDTO = commentServiceImpl.getCommentById(commentId);
        if (commentDTO != null) {
            return new ResponseEntity<>(commentDTO, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<List<CommentDTO>> getAllCommentsForPost(@PathVariable Long postId) {
        List<CommentDTO> commentDTOs = commentServiceImpl.getAllCommentsForPost(postId);
        return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
    }

    @GetMapping("/replies/{parentCommentId}")
    public ResponseEntity<List<CommentDTO>> getAllRepliesForParentComment(@PathVariable Long parentCommentId) {
        List<CommentDTO> replyComments = commentServiceImpl.getAllRepliesForParentComment(parentCommentId);
        return new ResponseEntity<>(replyComments, HttpStatus.OK);
    }

}
