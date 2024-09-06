package com.avinash.danumalk.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse {
    private UUID id;
    private UUID parentCommentId;
    private UUID postId;
    private String content;
    private String commentOwner;
    private Boolean isCurrentUserOwns = false;
}
