package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.PostDTO;
import com.avinash.danumalk.dto.PostMapper;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.repository.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public List<PostDTO> getAllPosts() {
        // Retrieve all posts, including subtypes, sorted by createdAt in descending order
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream()
                .map(postMapper::postToDTO) // Convert each Post to PostDTO using the mapper
                .collect(Collectors.toList());
    }

    public PostDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            return postMapper.postToDTO(post); // Convert Post to PostDTO using the mapper
        }
        return null;
    }


    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }
}
