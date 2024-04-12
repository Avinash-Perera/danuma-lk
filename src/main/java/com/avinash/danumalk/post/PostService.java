package com.avinash.danumalk.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostService implements PostServiceInterface {
    private final PostRepository postRepository;
    private final PostMapper postMapper;



    @Override
    public List<PostDTO> getAllPosts() {
        var posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream()
                .map(postMapper::postToDTO) // Convert each Post to PostDTO using the mapper
                .collect(Collectors.toList());
    }


    @Override
    public PostDTO getPostById(Long postId) {
        var post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            return postMapper.postToDTO(post); // Convert Post to PostDTO using the mapper
        }
        return null;
    }


    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }
}