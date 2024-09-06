package com.avinash.danumalk.comment;

import com.avinash.danumalk.common.PageResponse;
import com.avinash.danumalk.common.ResultResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin
@AllArgsConstructor
public class CommentController {
    private final CommentServiceImpl commentServiceImpl;

    @PostMapping("/{postId}")
    public ResponseEntity<ResultResponse<CommentResponse>> createCommentOnPost(@PathVariable UUID postId, @RequestBody CommentRequest commentRequest, Authentication connectedUser) {
        try {
            var createdComment = commentServiceImpl.createCommentOnPost(postId, commentRequest, connectedUser);
            return ResponseEntity.ok(createdComment);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/reply/{parentCommentId}")
    public ResponseEntity<ResultResponse<CommentResponse>> createReplyToComment(@PathVariable UUID parentCommentId, @RequestBody CommentRequest commentRequest, Authentication connectedUser) {
        try {
            var createdReplyComment = commentServiceImpl.createReplyToComment(parentCommentId, commentRequest, connectedUser);
            return ResponseEntity.ok(createdReplyComment);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/update/{commentId}")
    public ResponseEntity<ResultResponse<CommentResponse>> updateComment(@PathVariable UUID commentId, @RequestBody CommentRequest commentRequest, Authentication connectedUser) {
        try {
            var createdReplyComment = commentServiceImpl.updateComment(commentId, commentRequest, connectedUser);
            return ResponseEntity.ok(createdReplyComment);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PageResponse<CommentResponse>> getAllCommentsForPost(
            @PathVariable UUID postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        try{
            PageResponse<CommentResponse> response = commentServiceImpl.getAllCommentsForPost(postId, page, size, authentication);
            return ResponseEntity.ok(response);
        }catch  (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }


    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Boolean> deleteComment(@PathVariable UUID commentId, Authentication authentication) {
        try{
            boolean deleted = commentServiceImpl.deleteComment(commentId, authentication);
            return ResponseEntity.ok(deleted);
        }catch (IllegalStateException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}
