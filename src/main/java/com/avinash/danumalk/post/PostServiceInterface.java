package com.avinash.danumalk.post;

import com.avinash.danumalk.common.PageResponse;

import java.util.List;
import java.util.Optional;

public interface PostServiceInterface {
    PageResponse<PostDTO> getAllPostsPaginated(int page, int size);
    PostDTO getPostById(Long postId);
    Optional<Post> findById(Long postId);

}
