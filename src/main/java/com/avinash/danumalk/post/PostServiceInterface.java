package com.avinash.danumalk.post;

import java.util.List;
import java.util.Optional;

public interface PostServiceInterface {
    List<PostDTO> getAllPosts();
    PostDTO getPostById(Long postId);
    Optional<Post> findById(Long postId);

}
