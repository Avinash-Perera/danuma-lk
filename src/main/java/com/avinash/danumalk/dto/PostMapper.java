package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.Post;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {
    public PostDTO postToDTO(Post post) {
        return new PostDTO(
                post.getPostId(),
                post.getTitle(),
                post.getPostType(),
                post.getCreatedAt(),
                post.getUpdatedAt()

        );
    }

    public Post dtoToPost(PostDTO postDTO) {
        Post post = new Post();
        post.setPostId(postDTO.getPostId());
        post.setTitle(postDTO.getTitle());
        post.setPostType(postDTO.getPostType());
        post.setCreatedAt(postDTO.getCreatedAt());
        post.setUpdatedAt(postDTO.getUpdatedAt());
        return post;
    }

}
