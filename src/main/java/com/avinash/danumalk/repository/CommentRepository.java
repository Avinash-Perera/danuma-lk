package com.avinash.danumalk.repository;

import com.avinash.danumalk.model.Comment;
import com.avinash.danumalk.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);
}
