package com.avinash.danumalk.service;

import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> getAllPosts() {
        // Retrieve all posts, including subtypes, sorted by createdAt in descending order
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

}
