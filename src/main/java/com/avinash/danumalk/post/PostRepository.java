package com.avinash.danumalk.post;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import com.avinash.danumalk.post.Post;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("""
            SELECT post
            FROM Post post
            ORDER BY post.createdAt DESC
           """)
    Page<Post> findAllPosts(Pageable pageable);
}