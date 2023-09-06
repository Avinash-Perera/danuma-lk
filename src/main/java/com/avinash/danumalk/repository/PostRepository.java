package com.avinash.danumalk.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.avinash.danumalk.model.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{

    List<Post> findAllByOrderByCreatedAtDesc();
}
