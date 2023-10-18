package com.avinash.danumalk.post;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avinash.danumalk.post.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{

    List<Post> findAllByOrderByCreatedAtDesc();
}
