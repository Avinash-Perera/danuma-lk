package com.avinash.danumalk.service;

import com.avinash.danumalk.dto.PostDTO;
import com.avinash.danumalk.dto.PostMapper;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.repository.ImagePostRepository;
import com.avinash.danumalk.repository.PostRepository;
import com.avinash.danumalk.repository.TextPostRepository;
import com.avinash.danumalk.repository.VideoPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostMapper postMapper; // Inject the PostMapper

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


}
