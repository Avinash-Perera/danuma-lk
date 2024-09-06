package com.avinash.danumalk.comment;

import java.util.UUID;

public interface CustomCommentRepository {
    Comment createCommentWithPost(UUID postId, Comment comment);
}
