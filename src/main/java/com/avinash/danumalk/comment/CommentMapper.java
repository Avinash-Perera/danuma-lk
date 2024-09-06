package com.avinash.danumalk.comment;

import com.avinash.danumalk.posts.BasePostEntityRepository;
import com.avinash.danumalk.user.User;
import com.avinash.danumalk.user.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@AllArgsConstructor
public class CommentMapper {
    private final BasePostEntityRepository basePostEntityRepository;
    private final UserRepository userRepository;



    public Comment mapToComment(CommentRequest commentRequest) {
        return Comment.builder()
                .content(commentRequest.content())
                .build();
    }

    public CommentResponse mapToCommentResponse(Comment comment,  Authentication connUser) {
        User user = ((User) connUser.getPrincipal());
        boolean CurrentUserOwnStatus = user.getId().equals(comment.getOwner().getId());

        return CommentResponse.builder()
                .id(comment.getId())
                .parentCommentId(comment.getParentComment() != null ? comment.getParentComment().getId() : null)
                .postId(comment.getPost().getId())
                .content(comment.getContent())
                .commentOwner(comment.getOwner().getUsername())
                .isCurrentUserOwns(CurrentUserOwnStatus)
                .build();
    }



}
