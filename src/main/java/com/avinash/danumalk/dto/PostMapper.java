package com.avinash.danumalk.dto;

import com.avinash.danumalk.model.ImagePost;
import com.avinash.danumalk.model.Post;
import com.avinash.danumalk.model.TextPost;
import com.avinash.danumalk.model.VideoPost;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PostMapper {
    private final ImagePostMapper imagePostMapper;
    private final VideoPostMapper videoPostMapper;
    private final TextPostMapper textPostMapper;


    public PostDTO postToDTO(Post post) {
        if (post instanceof ImagePost) {
            return imagePostMapper.imagePostToDTO((ImagePost) post);
        } else if (post instanceof VideoPost) {
            return videoPostMapper.videoPostToDTO((VideoPost) post);
        } else if (post instanceof TextPost) {
            return textPostMapper.textPostToDTO((TextPost) post);
        } else {
            // Handle other post types if needed
            return createErrorPostDTO();

        }
    }

    private PostDTO createErrorPostDTO() {
        PostDTO errorDTO = new PostDTO();
        errorDTO.setError(true);
        errorDTO.setErrorMessage("Unsupported post type");
        return errorDTO;
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