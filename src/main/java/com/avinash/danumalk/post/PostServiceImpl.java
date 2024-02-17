package com.avinash.danumalk.post;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostServiceInterface {
    private final PostRepository postRepository;
    private final PostMapper postMapper;


    /**
     * Retrieves all posts, including subtypes, sorted by createdAt in descending order.
     *
     * @return a list of PostDTO objects representing the retrieved posts
     */
    @Override
    public List<PostDTO> getAllPosts() {
        var posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream()
                .map(postMapper::postToDTO) // Convert each Post to PostDTO using the mapper
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a PostDTO object by its ID.
     *
     * @param postId the ID of the post
     * @return the PostDTO object representing the post, or null if the post does not exist
     */
    @Override
    public PostDTO getPostById(Long postId) {
        var post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            return postMapper.postToDTO(post); // Convert Post to PostDTO using the mapper
        }
        return null;
    }

    /**
     * Retrieves a post by its ID.
     *
     * @param postId the ID of the post to retrieve
     * @return an Optional containing the post, or an empty Optional if no post was found
     */
    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }
}