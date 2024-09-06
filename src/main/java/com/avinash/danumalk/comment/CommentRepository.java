package com.avinash.danumalk.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, UUID>, CusCommentRepository {
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId")
    Page<Comment> findAllByPostId(UUID postId, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE c.parentComment.id = :parentCommentId")
    Page<Comment> findAllByParentCommentId (UUID parentCommentId, Pageable pageable);

}