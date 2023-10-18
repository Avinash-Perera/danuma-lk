package com.avinash.danumalk.comment;

import com.avinash.danumalk.comment.Comment;
import com.avinash.danumalk.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByPost(Post post);

    List<Comment> findAllByParentComment(Comment parentComment);
}
