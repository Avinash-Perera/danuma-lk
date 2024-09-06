package com.avinash.danumalk.comment;
import org.springframework.security.core.Authentication;

public interface CusCommentRepository {
    CommentResponse saveAndMapToResponse(Comment comment, Authentication connectedUser);
}
