package com.avinash.danumalk.post;

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

    /**
     * Retrieves all posts, including subtypes, sorted by createdAt in descending order.
     *
     * @return a list of PostDTO objects representing the retrieved posts
     */
    public List<PostDTO> getAllPosts() {
        // Retrieve all posts, including subtypes, sorted by createdAt in descending order
        List<Post> posts = postRepository.findAllByOrderByCreatedAtDesc();
        return posts.stream()
                .map(postMapper::postToDTO) // Convert each Post to PostDTO using the mapper
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a PostDTO object by its ID.
     *
     * @param  postId  the ID of the post
     * @return         the PostDTO object representing the post, or null if the post does not exist
     */
    public PostDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId).orElse(null);
        if (post != null) {
            return postMapper.postToDTO(post); // Convert Post to PostDTO using the mapper
        }
        return null;
    }


    /**
     * Retrieves a post by its ID.
     *
     * @param  postId   the ID of the post to retrieve
     * @return          an Optional containing the post, or an empty Optional if no post was found
     */
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }
}
